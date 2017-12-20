(ns status-im.utils.views
  (:require [clojure.walk :as w]))

(defn atom? [sub]
  (or (vector? sub)
      (and (seq sub)
           (#{`reagent.core/atom} (first sub)))))

(defn walk-sub [sub form->sym]
  (if (coll? sub)
    (w/postwalk (fn [f]
                  (or (form->sym f) f)) sub)
    (or (form->sym sub) sub)))

(defn prepare-subs [subs]
  (let [pairs     (map (fn [[form sub]]
                         {:form form
                          :sub  sub
                          :sym  (if (atom? sub)
                                  (gensym (str (if (map? form) "keys" form)))
                                  form)})
                       (partition 2 subs))
        form->sym (->> pairs
                       (map (fn [{:keys [form sym]}]
                              [form sym]))
                       (into {}))]
    [(mapcat (fn [{:keys [form sym sub]}]
               (if (vector? sub)
                 [sym `(re-frame.core/subscribe ~(walk-sub sub form->sym))]
                 [form (walk-sub sub form->sym)]))
             pairs)
     (apply concat (keep (fn [{:keys [sym form sub]}]
                           (when (atom? sub)
                             [form `(deref ~sym)]))
                         pairs))]))

(defmacro letsubs [args & body])

(defmacro defview
  [n params & rest-body]
  (let [first-symbol (ffirst rest-body)
        rest-body'   (if (and (symbol? first-symbol)
                              (= (name first-symbol) "letsubs"))
                       (rest (first rest-body))
                       rest-body)
        [subs component-map body] (case (count rest-body')
                                    1 [nil {} (first rest-body')]
                                    2 [(first rest-body') {} (second rest-body')]
                                    3 rest-body')
        [subs-bindings vars-bindings] (prepare-subs subs)]
    `(do
       (when-not (find-ns 're-frame.core)
         (require 're-frame.core))
       (defn ~n ~params
         (let [~@subs-bindings]
           (reagent.core/create-class
            (merge ~(->> component-map
                         (map (fn [[k f]]
                                (let [args (gensym "args")]
                                  [k `(fn [& ~args]
                                        (let [~@vars-bindings]
                                          (apply ~f ~args)))])))
                         (into {}))
                   {:display-name (name '~n)
                    :reagent-render
                                  (fn ~params
                                    (let [~@vars-bindings]
                                      ~body))})))))))

(defn check-view [all {:keys [view views component hide? parent]}]
  (let [parent           (or parent :root)
        views            (or views #{view})
        comp             {:views     views
                          :component component
                          :hide?     hide?}]
    (-> all
        (assoc-in [:components views] comp)
        (update-in [:view->children parent]
                   (fn [children]
                     (let [children (or children [])]
                       (conj children views))))
        (update :view->components
                (fn [view->components]
                  (reduce (fn [view->components view]
                            (assoc view->components view views))
                          view->components views))))))

(defn -build-tree [views]
  (reduce check-view {:components       {}
                      :view->components {}
                      :view->children   {}} views))

(defn -get-all-views
  [{:keys [view->children] :as config} component]
  (let [children (reduce clojure.set/union (keep view->children component))]
    (reduce clojure.set/union
            (concat [component]
                    children
                    (map (fn [child]
                           (-get-all-views config child))
                         (reduce clojure.set/union
                                 (map view->children component)))))))

(defn -get-granchildren [{:keys [view->children] :as config} children]
  (into {}
        (filter
         (fn [[_ children]]
           (not (nil? children)))
         (map (fn [child]
                       [child (get view->children child)])
                     (reduce clojure.set/union children)))))

(defn -generate-component
  [{:keys [components view->children] :as config} view-sym component-name]
  (let [component-config (get components component-name)
        children         (get view->children component-name)
        grandchildren    (-get-granchildren config children)]
    `[status-im.ui.components.react/view
      {:flex
       ~(if (= :root component-name)
          1
          `(if (contains?
                ~(disj (-get-all-views config #{component-name}) component-name)
                ~view-sym)
             1
             0))}
      ~@(when component-config
          `[status-im.ui.components.react/wrap-comp
            ~(assoc component-config :current-view view-sym)])
      ~@(map (fn [child]
               `[status-im.ui.components.react/wrap-comp
                 ~(assoc (get components child) :current-view view-sym)])
             children)
      ~@(map (fn [[grandchild grandgrandchildren]]
                  `[status-im.ui.components.react/wrap-comp
                    {:component    [status-im.ui.components.react/with-empty-preview
                                    ~(-generate-component config view-sym grandchild)]
                     :views        ~(conj
                                     (reduce
                                      clojure.set/union
                                      (map (fn [grandgrandchild]
                                             (-get-all-views config grandgrandchild))
                                           grandgrandchildren))
                                     grandchild)
                     :hide?        true
                     :current-view ~view-sym}])
                  grandchildren)]))

(defn -compile-views [n views]
  (let [view-sym (gensym "view-id")]
    `(defview ~n []
       (letsubs [~view-sym [:get :view-id]]
        ~(let [tree (-build-tree views)]
           (-generate-component tree view-sym :root))))))

(defmacro compile-views
  [n views]
  (-compile-views n views))

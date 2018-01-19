(ns status-im.ui.screens.profile.qr-code.views
  (:require [re-frame.core :as re-frame]
            [status-im.ui.components.qr-code :as qr-code]
            [status-im.ui.components.react :as react]
            [status-im.ui.components.icons.vector-icons :as vi]
            [status-im.ui.components.status-bar.view :as status-bar]
            [status-im.ui.components.list-selection :as list-selection]
            [status-im.ui.screens.profile.qr-code.styles :as styles]
            [status-im.utils.platform :as platform]
            [status-im.i18n :as i18n])
  (:require-macros [status-im.utils.views :refer [defview letsubs]]))


(defn qr-viewer-toolbar [name qr-value]
  [react/view styles/account-toolbar
   [react/view styles/toolbar-contents
    [react/view styles/toolbar-action-container
     [react/touchable-highlight {:on-press #(re-frame/dispatch [:navigate-back])}
      (if platform/ios?
        [react/view
         [react/text {:style styles/toolbar-done-text-ios}
         (i18n/label :t/done)]]
        [react/view styles/toolbar-action-icon-container
         [vi/icon :icons/close {:color :black}]])]]
    [react/view styles/name-container
     [react/text {:style           styles/name-text
                  :number-of-lines 1} name]]
    [react/view styles/toolbar-action-container
     [react/touchable-highlight {:on-press #(list-selection/open-share {:message qr-value})}
      [react/view styles/toolbar-action-icon-container
       [vi/icon :icons/share {:color :black}]]]]]])

(defn qr-viewer-body [qr-value dimensions]
  [react/view {:style     styles/qr-code
               :on-layout #(let [layout (.. % -nativeEvent -layout)]
                             ;;TODO(goranjovic): check if we really really need to do this
                             (re-frame/dispatch [:set-in [:qr-modal :dimensions] {:width  (* 0.7 (.-width layout))
                                                                                  :height (.-height layout)}]))}
   [react/text {:style styles/qr-code-hint} "Share one these codes\nto start chatting"]
   (when (:width dimensions)
     [react/view {:style (styles/qr-code-container dimensions)}
      [qr-code/qr-code {:value qr-value
                        :size  (- (min (:width dimensions)
                                       (:height dimensions))
                                  (* 2 styles/qr-code-padding))}]])])

(defn qr-viewer-footer [qr-value]
  [react/view styles/footer
   [react/view styles/wallet-info
    [react/text {:style styles/hash-value-text} qr-value]]])

(defview qr-viewer []
  (letsubs [{:keys [qr-source qr-value dimensions contact]} [:get :qr-modal]]
    [react/view styles/wallet-qr-code
     [status-bar/status-bar {:type :modal}]
     [qr-viewer-toolbar (:name contact) qr-value]
     [qr-viewer-body qr-value dimensions]
     [qr-viewer-footer qr-value]]))

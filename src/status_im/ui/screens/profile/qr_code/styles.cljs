(ns status-im.ui.screens.profile.qr-code.styles
  (:require [status-im.ui.components.styles :as styles]
            [status-im.ui.components.colors :as colors])
  (:require-macros [status-im.utils.styles :refer [defstyle]]))

(def qr-code-hint
  {:color          styles/color-gray4
   :padding-bottom 24
   :text-align     :center})

(def qr-code-padding
  15)

(defn qr-code-container [dimensions]
  {:background-color colors/white
   :width            (:width dimensions)
   :align-items      :center
   :justify-content  :center
   :padding          qr-code-padding
   :border-radius    10})

(defstyle name-container
  {:flex           0.7
   :flex-direction :column
   :android        {:margin-left    15}})

(defstyle name-text
  {:color     styles/color-black
   :font-size 17
   :ios       {:letter-spacing -0.2}})

(def address-text
  {:color     colors/white
   :font-size 12})

(def toolbar-action-container
  {:flex            0.15
   :flex-direction  :column
   :align-items     :center
   :justify-content :center})

(def toolbar-action-icon-container
  {:width           40
   :height          40
   :margin-right    4
   :align-items     :center
   :justify-content :center})

(def wallet-qr-code
  {:flex-grow      1
   :flex-direction :column})

(def account-toolbar
  {:background-color colors/white})

(def toolbar-contents
  {:flex-grow       1
   :flex-direction  :row
   :height          55
   :align-items     :center
   :justify-content :center})

(def qr-code
  {:background-color styles/color-light-gray
   :flex-grow        1
   :align-items      :center
   :justify-content  :center})

(def footer
  {:background-color styles/color-light-gray
   :flex-direction   :row
   :justify-content  :center
   :padding-bottom   80})

(def wallet-info
  {:align-items    :center
   :padding-bottom 20})

(def hash-value-type
  {:color          styles/color-black
   :padding-bottom 5})

(def hash-value-text
  {:color             styles/color-black
   :margin-horizontal 60
   :text-align        :center
   :font-size         15
   :letter-spacing    -0.2
   :line-height       20})

(def done-button-text
  {:color colors/white})

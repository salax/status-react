(ns status-im.ui.screens.profile.qr-code.styles
  (:require [status-im.ui.components.styles :as styles]
            [status-im.ui.components.colors :as colors]))

(def qr-code-hint
  {:color          styles/color-gray4
   :padding-bottom 24
   :text-align     :center})

(defn qr-code-container [dimensions]
  {:background-color colors/white
   :width            (:width dimensions)
   :align-items      :center
   :justify-content  :center
   :padding          40})

(def name-container
  {:flex           1
   :flex-direction :column})

(def name-text
  {:color     styles/color-black
   :font-size 16})

(def address-text
  {:color     colors/white
   :font-size 12})

(def online-container
  {:flex            0.2
   :flex-direction  :column
   :align-items     :center
   :justify-content :center})

(def online-image-container
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

(def wallet-account-container
  {:flex-grow       1
   :flex-direction  :row
   :height          69
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
   :justify-content  :center})

(def wallet-info
  {:align-items    :center
   :padding-bottom 20})

(def hash-value-type
  {:color          styles/color-black
   :padding-bottom 5})

(def hash-value-text
  {:color             styles/color-black
   :margin-horizontal 60
   :text-align        :center})

(def done-button-text
  {:color colors/white})

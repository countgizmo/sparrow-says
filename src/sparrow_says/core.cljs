(ns sparrow-says.core
  (:require [cljs-time.core :as time]))

(def debug?
  ^boolean js/goog.DEBUG)

(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode")))

(defn dice-roll
  []
  (rand-int 6))

(defn dice-roll->coin-flip
  [dice-roll]
  (if (> 3 dice-roll) 0 1))

(defn exercise
  [week-number roll]
  (let [idx     (dice-roll->coin-flip roll)
        choices (if (even? week-number)
                  [:snatches :sw+pu]
                  [:sw+pu :snatches])]
    (nth choices idx)))

(defn series
  [context roll]
  (assoc context :series (nth [2 3 3 4 4 5] roll)))

(defn reps-scheme
  [context roll]
  (assoc context :reps-scheme (nth [:5x4 :5x4 :alt :alt :10x2 :10x2] roll)))

(defn swing-type
  [context roll]
  (cond-> context
    (= :sw+pu (:exercise context))
    (assoc :sw-type (nth [:2-arm :1-arm] (dice-roll->coin-flip roll)))))

(defn workout
  [week-number]
  (-> {:exercise (exercise week-number (dice-roll))}
      (series (dice-roll))
      (reps-scheme (dice-roll))
      (swing-type (dice-roll))))

(defn format-workout
  [week {:keys [exercise series reps-scheme sw-type]}]
  (str "Week " week
       "<br />"
       "Exercise: " exercise
       "<br />"
       "Series: " series
       "<br />"
       "Reps: " reps-scheme
       "<br />"
       (when sw-type (str "SW Type: " sw-type))))

(defn render!
  []
  (let [app  (.getElementById js/document "app")
        week (time/week-number-of-year (time/now))]
    (set! (.-innerHTML app) (format-workout week (workout week)))))

(render!)

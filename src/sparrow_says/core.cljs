(ns sparrow-says.core
  (:require [cljs-time.core :as time]))

(def debug?
  ^boolean js/goog.DEBUG)

(defn dev-setup
  []
  (when debug?
    (enable-console-print!)
    (println "dev mode")))

(defn dice-roll
  "Returns a random number from 0 to 5 inclusive."
  []
  (rand-int 6))

(defn dice-roll->coin-flip
  [roll]
  (if (> 3 roll) 0 1))

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

(defn div
  ([text]
   (div text ""))
  ([text class]
   (str "<div"
        " class=\"" class "\">"
        text
        "</div>")))

(defn h3
  ([text]
   (div text ""))
  ([text class]
   (str "<h3"
        " class=\"" class "\">"
        text
        "</h3>")))

(defn button
  ([label onclick]
   (button label onclick ""))
  ([label onclick class]
   (str "<button"
        " class=\"" class "\""
        " onclick=" onclick ">"
        label
        "</button>")))

(defn img
  [src class]
  (str "<img"
       " class=\"" class "\""
       " src=" src " />"))

(defn format-workout
  [{:keys [exercise series reps-scheme sw-type]}]
  (let [img-src (case exercise
                  :snatches "img/kb-snatch.png"
                  :sw+pu "img/kb-swing.png")]
    (div
     (str
          (div (img img-src "w-32") "bg-indigo-300 inline-block rounded-md")
          (div
           (str (div (str series " series"))
                (div (str (name reps-scheme) " reps"))
                (when sw-type (div (name sw-type))))
           "bg-indigo-100 w-32 px-4 py-2 rounded-md")

          (button "Again!" "sparrow_says.core.render_BANG_();" "mt-4 py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 w-32"))
     "container ml-8 mt-4 w-36 border border-indigo-900 p-2 rounded-md")))

(defn ^:export render!
  []
  (let [app  (.getElementById js/document "app")
        week (time/week-number-of-year (time/now))]
    (set! (.-innerHTML app) (format-workout (workout week)))))

(render!)

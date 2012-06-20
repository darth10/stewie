(ns stewie.core)

(defn accumulator []
  "Generages an accumulator that will return average and variance in O(1)

  It keeps an internal state of:
  {
    :total sum(x_i),
    :sq_total sum(x_i ^ 2),
    :count count(x_i),
  }

  Result is:
  {
    :average      :total / :count
    :variance     :sq_total / :count - :average ^ 2
  }

  "
  (let [totals (atom {:total 0, :count 0, :sq_total 0})]
    (fn [n]
      (let [update_totals (comp #(update-in % [:count] inc)
                                #(update-in % [:total] + n)
                                #(update-in % [:sq_total] + (Math/pow n 2)))
            result (swap! totals update_totals)
            cnt (result :count)
            avg (/ (result :total) cnt)]
        {:average avg
         :variance (- (/ (result :sq_total) cnt) (Math/pow avg 2))}))))


; Reference implementations
(defn average
  "naive average implementation"
  [coll]
  (/ (reduce + coll) (count coll)))

(defn variance
  "naive variance implementation"
  [coll]
  (let [avg (average coll)]
     (/ (reduce + (map #(Math/pow (- % avg) 2) coll))
      (count coll))))

(defn sliding-window-average
  "calculates the average of the first n elements of coll"
  [coll n]
  (average (take n coll)))

(defn sliding-window-variance
  "calculates the variance of the first n elements of coll"
  [coll n]
  (variance (take n coll)))

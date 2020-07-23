(ns finbuddy.validator
  (:require
   [cljs.spec.alpha :as s]
   [validator :as v]
   [goog :refer [isFunction]]))

;; Building blocks
(s/def ::fields (s/+ keyword?))
(s/def ::message string?)
(s/def ::when isFunction)
(s/def ::value any?)
(s/def ::chars string?)
(s/def ::comparison string?)
(s/def ::date string?)
(s/def ::locale string?)
(s/def ::opts map?)
(s/def ::radix int?)
;; Flags
(s/def ::keep-new-lines boolean?)
(s/def ::strict boolean?)

;; Sanitizer rules
(s/def ::blacklist (s/keys :req [::fields ::chars]))
(s/def ::escape (s/or :full (s/keys :req [::fields])
                      :simple ::fields))            ;; Make simplified form?
(s/def ::trim (s/keys :req [::fields]
                      :opt [::chars]))
(s/def ::ltrim ::trim)
(s/def ::rtrim ::trim)
(s/def ::normalize-email (s/keys :req [::fields]
                                 :opt [::opts]))
(s/def ::strip-low (s/keys :req [::fields]
                           :opt [::keep-new-lines]))
(s/def ::to-boolean (s/keys :req [::fields]
                            :opt [::strict]))
(s/def ::to-date (s/keys :req [::fields]))           ;; Make simplified form?
(s/def ::to-float (s/keys :req [::fields]))          ;; Make simplified form?
(s/def ::to-int (s/keys :req [::fields]
                        :opt [::radix]))
(s/def ::unescape (s/keys :req [::fields]))          ;; Make simplified form?
(s/def ::whitelist (s/keys :req [::fields ::chars]))

;; Validation rules
(s/def ::contains (s/keys :req [::fields]
                          :opt [::opts]))
(s/def ::equals (s/keys :req [::fields ::comparison]))
(s/def ::is-after (s/keys :req [::fields]
                          :opt [::date]))
(s/def ::is-alpha (s/keys :req [::fields]
                          :opt [::locale]))
(s/def ::is-alphanumeric (s/keys :req [::fields]
                                 :opt [::locale]))
(s/def ::is-ascii (s/keys :req [::fields]))            ;; Make simplified form?
(s/def ::is-base32 (s/keys :req [::fields]))           ;; Make simplified form?
(s/def ::is-base64 (s/keys :req [::fields]
                           :opt [::opts]))
(s/def ::is-before (s/keys :req [::fields]
                           :opt [::locale]))
(s/def ::is-bic (s/keys :req [::fields]))

(defn rule-valid?
  [rule body]
  (s/valid? rule body))

(defn apply-validator
  [form fields validator]
  (if (and fields chars)
    (reduce (fn [form field]
              (if (contains? form field)
                (assoc form field (validator (get form field)))
                form))
            form
            fields)
    form))

(defn sanitize
  [form rules]
  (if (nil? rules)
    form
    (reduce (fn [form rule-name]
              (let [rule (get rules rule-name)
                    fields (::fields rule)]
                (case rule-name
                  ::blacklist (apply-validator form fields #(v/blacklist % (::chars rule)))
                  ::escape (apply-validator form fields #(v/escape %))
                  ::normalize-email (apply-validator form fields #(v/normalizeEmail %))
                  ::trim (apply-validator form fields #(v/trim %))
                  ::to-boolean (apply-validator form fields #(v/toBoolean %)))))
            form
            (keys rules))))

(comment (defn validate
           [form rules]))
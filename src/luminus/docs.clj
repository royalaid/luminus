(ns luminus.docs
  (:use compojure.core hiccup.element)
  (:require [luminus.common :as common]
            [luminus.util :as util]))

(defn doc-link [route selected? title]
  [:li (link-to {:class (if selected? "selected" "unselected")} route title)])

(defn doc-page-links [doc-titles doc]
  (let [selected-title (get (into {} doc-titles) doc)]
    (into
      [:ul]
      (for [[doc page-title] doc-titles]
        (doc-link (str "/docs/" doc)
                  (= page-title selected-title)
                  page-title)))))

(defn doc-page [doc]
  (common/layout "Documentation"
                 (let [doc-titles (:pages @util/docs)
                       doc-content (get @util/docs doc)
                       toc (util/generate-toc doc-content)]
                   [:div [:div#left
                          [:div.entry [:h1 (get (into {} doc-titles) doc)]
                           (if (> (count toc) 1)
                             [:div [:h2 "Contents"] toc [:h2]])
                           doc-content]]
                    [:div#right
                     [:div.sidemenu [:h3 "Topics"]
                      (doc-page-links doc-titles doc)
                      [:div [:h3 "Books"]
                       [:a.booklink {:href "http://pragprog.com/book/dswdcloj/web-development-with-clojure"}
                        (image {:class "bookimage"} "/img/wdwc.png")]]]]])))

(defroutes doc-routes
           (GET "/docs" [] (doc-page "guestbook.md"))
           (GET "/docs/:doc" [doc] (doc-page doc)))

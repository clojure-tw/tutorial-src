(ns crash-tutorial)
;;; --------------------------------------------------------------------------------
;;; --------------------------------------
;;; |              REPL                  |
;;; --------------------------------------
;; REPL 全稱：Read-Eval-Print-Loop，是一個早期 LISP 語言就有的功能，
;; 可以讓程式開發變成互動式。
;;
;; 以下有幾個名詞說明：n
;; 1. symbol: 非數字開頭，可包含 *, +, !, -, _, ', ?, <, >,  =  的連續字組
;; 2. s-exp: 全稱 symbolic expression
;; 3. eval: evaluate 的簡寫
;; 4. cursor: 鍵盤輸入的那根 bar 或是 方塊
;;
;;
;;
;; 說明一下 s-exp：
;; 一個 s-exp 為一組用小括號包起來的 n 個 expression (以下簡稱 expr)。
;; 而一個 expr 可以是 symbol、顯式資料、資料結構或是一個 s-exp，是能夠被 eval 的最基本單位。
;;
;; e.g.
print                                   ; 這是一個合法 expression
                                        ; p.s. 其實在 LISP 的原始定義，這種沒有括號包著，可以被 eval 的單元叫 atom，
                                        ; 但 Clojure 的 atom 有另一個意思，為了不和 clojure 的 atom 搞混，
                                        ; 且減少剛開始學習的複雜度才會說他是 expression。

(print "Hello World")                   ; 這也是一個 sexp



(loop [n 100]                           ; 這個也是
  (if (< n 0)                           ; 一整個 loop s-exp 中包含了另一段 s-exp
    (println "done")                    ; Clojure 語法基本上就是一層層 s-exp 組起來的
    (do
      (print n "!")
      (recur (dec n)))))

;; 資料和資料結構也都是 s-exp，在接下來的教學會說明
100                                     ; 一個 Integer

'()                                     ; Empty List

["This" "is" "a" "vector"]              ; Vector

"Hello World"                           ; String

;; eval
;; 對某個 s-exp 求值 (eval)
;; 把 cursor 移到到 s-exp 的最右邊 or 最左邊再按快速鍵就可以求值，
;; Visual Studio Code: Calva 中的快速鍵為 Control + Alt + V, E
;; 或是直接打在 REPL 上，也可以對 s-exp 求值。
;; | <- 這個 bar 表示 cursor
;; e.g.
;; |(print "hello")
;; (print "hello")|
;;
;; 請求值以下 s-exp 看看結果為何?

100
"a string"
(range 100)
(reverse (range 100))
(filter #(= 0 (mod % 3)) (range 100))

;;; REPL Namespace
;; 開啟 REPL 後，會看到以下訊息：
;; user>
;;
;; 這意思是目前的 namespace 為 user，namespace 可以先當做檔案名稱，因此這個 REPL 目前在 user.clj 這個檔案下。
;; p.s. 一般這種等使用者打字的東西會叫 prompt
;;
;; 在 REPL 中，user 可以切換 namespace，在多數情況下(視編輯器而定)，
;; 使用者會需要 eval 檔案最開頭的 ns expression，來切換 REPL 的 namespace:
;; (ns namespace.filename)
;;

;;; source & doc
;; source 為在 REPL 中看 function 原始碼的方法
;; doc 為看文件的方法
;;
;; p.s. source 和 doc 無法看一些用 java 寫成的原始碼和 javadoc
(source ->)
(doc ->)

;;; Reference:
;; https://clojure.org/reference/reader#_symbols
;;
;;; --------------------------------------------------------------------------------


;;; --------------------------------------------------------------------------------
;;; --------------------------------------
;;; |              註解                  |
;;; --------------------------------------
;; 註解不是程式碼，是為了在解式碼間記錄一些資訊，可能是程式的說明。
;; Clojure 的註解用 ";" Semicolon 做為開頭，在 ";"  後面的字串都不會被當做程式碼。

;; Clojure 還有另一種註解方式 - comment：
(comment
  ;; 執行這個檔案時，下面這行不會被執行到。
  (print "Hello World"))

(print "Hello World")  ; 而這行會出現在 OUTPUT 中

;; 三個逗號會被無視，嚴格來說不能算註解，因為是一個被無視的符號，
;; 通常會在 thread macro 中被用來標做 function parameter。
,,,

(-> 0
    (+ ,,, 2)                           ; 這邊的 ,,, 只是表示前面的 0 會被當成參數的位置
    (* ,,, 1))

;;; --------------------------------------------------------------------------------

;;; --------------------------------------------------------------------------------
;;; --------------------------------------
;;; |       資料型態 (Data Type)         |
;;; --------------------------------------
;; 因為 Clojure 本身是寄身在 Java[1] 上的語言，因此資料型態其實是討論不完的，
;; Java 是一個 Class Base 的物件導向語言，一個 Class 就是一種資料型態的定義，基本上可以當成是無限個 Type，
;; 學 Java 的人一開始會先關心 Primitive Data Type[2]，那是幾個非物件的資料型態，但在 Clojure 中沒辦法操作這種 Type，
;; 所以東西其實背後都是一個 Java 的物件，因此在 Clojure 中，一開始要關心的是 "字面上"[3] (literal) 的資料型態。
;;
;; 1. Integer
;; 2. BigInt
;; 3. Float Point
;; 4. Character
;; 5. String
;; 6. Boolean
;; 7. nil [4]
;; 8. Symbol
;; 9. Keyword
;; 10. Regular Expression Pattern
;; 11. Clojure 4 個基本資料結構 (再下一個小節說明)
;;
;; [1] 嚴格來說是 JVM (Java Virtual Machine)，另外 Clojure 還有寄身在 CLR (.NET) 和 JavaScript Engine 上的其它環境。
;; [2]
;; [3] "字面上" 的意思是說顯式的寫法，也就是用一些符號就可以表示的方式。
;; [4] nil 就在一般常見語言的 Null ( Golang 也學 LISP 用 nil )

;;
;; 1. Integer
;; Clojure 的 Integer 表示法就是一般常用的阿拉伯數字十進位記數法
1
0
-1
Long/MAX_VALUE                          ; 這是 Integer 的最大值
Long/MIN_VALUE                          ; 這是 Integer 的最小值
                                        ; p.s. 因為 Clojure 本身在 JVM 上的關係，所以可以偷用 JAVA 的 class ，
                                        ; Long 就是 JAVA 中的 java.lang.Long，
                                        ; 而 / 後面可以接的是 java class 的 static function 或 member，
                                        ; 在 Clojure 中 java class 也是一個合法的 Symbol，
                                        ; 這種可以使用 Java class 的功能叫 Java Introp，詳細可參考文件：
                                        ; https://clojure.org/reference/java_interop


;; 八進位
;; 八進位的記數法
;; 數字最前面加 0，數字不能有 8、9，因為逢 8 就會進位。
01234567

;; 十六進位
;; 十六進位的記數法
;; 數字最前面加 0x，因為阿拉伯數字只有十個字母，很不方便，所以還用了英文的 A~F 表示 10 ~ 15

-0x0123456789ABCDEF
0x0123456789ABCDEF

;; 2. BigInt
;; 就是數字在最後面加 N
;; e.g.
0N
1N

(= 0N 0)                                ; p.s. BigInt 還是可以和 Integer 一起計算
(+ 1 1N)                                ; 四則運算時 Integer 會被提升為 BigInt

;; 3. Floating Point
;;
0.0
-0.1
0.1
1.1
Double/MAX_VALUE
Double/MIN_VALUE

;; 科學記號寫法
1.0e-15

;; 為什麼浮點數不是小數?
;; https://0.30000000000000004.com
(= 0.3 (+ 0.1 0.2))                     ; => false

;; 4. Char （字元）
;; Character 中文叫字元，指一個符號，在計算機的世界中會給符號"編碼"，
;; 就像給一個人身份證字號一樣， 每個符號也會有各自的身份證字號。
;; 這世界上有很多種編碼系統，就像不同國家的身份號碼一樣，
;; 也有可能會有一個人有兩種以上的號碼，有時候看到亂碼就是因為編碼選錯。
;; JVM 的預設編碼和作業系統相同，以 Mac OSX 來說，預設就是 unicode "UTF-8"。
\a
\b
\字                                     ; 支援 unicode
\元                                     ; 支援 unicode
\λ                                      ; 支援 unicode

;; 以下 exp eval 會失敗
\🐡                                     ; emoji 的 unitcode 未支援，因此行不合法

;; 5. String （字串）
;; 一串字元
"This is a string"
"這是字串"
"🐡"

;; 以下的例子可以看出字串就是字元的組合
(str \T \h \i \s \  \i \s \  \a \  \s \t \r \i \n \g)
(clojure.string/split "This is a string" #"")
;; ^.__ 前面的 "clojure.string" 叫 namespace，namespace 是為了怕 symbol 名稱重覆，而且分不同檔案管理比較方便。
;; 意思是說你可以定義一個 user/split，一樣叫 split，但不會影響 clojure.string/split。

;; 6. Boolean
;; 就是對和錯
true
false

;; 7. nil
;; 沒有東西叫 nil，和其它語言的 Null 一樣（但 Golang 也叫 nil）。
nil

;; 8. Symbol
;; Symbol 最一開啟有說過："symbol: 非數字開頭，可包含 *, +, !, -, _, ', ?, <, >,  =  的連續字組"
;; 以下都是 Symbol
map
reduce
list
hash-map
undefine-symbol                         ; 這個求值會失敗，跳 Exception 出來，因為這個 symbol 沒有定義過。
'undefine-symbol                        ; 如果只是要參考名稱本身，而不去解析定義，在 symbol 前加一個 quote(')。 -> 請參考 quote special form

;; 9. Keyword
;; Keyword 的做用和 Clojure 4 個資料結構的 hash-map 有相當大的關係
:abc
:1                                      ; 和 Symbole 不同的是可以用數字為開頭
:1-A

;; 以下是一個 keyword 使用範例：
(def food
  {:fruit ['apple, 'orange, 'lemon]     ; hash-map 的寫法，詳細會在資料結構的部份說明
   :meat ['beef, 'pork, 'chicken]})
(:fruit food)                           ; keyword 可以直接當成 function 來取 hash-map 的值
                                        ; 以 Python 為例就是取 dict 的 get 方法：
                                        ; food.get('fruit')

;; 10. Regular Expression Pattern
;; 寫法為 hash + double quote： #"regex"
;; Clojure 中 Regular Expression 是 java.util.regex 的，和常見的 PCRE 語法有一點點不一樣。
#"[0-9]+"
#"[a-z]+"
#"([0-9]{5})-[a-zA-Z]+"

;;; --------------------------------------------------------------------------------


;;; --------------------------------------------------------------------------------
;;; --------------------------------------
;;; |         Function Concept           |
;;; --------------------------------------
;; Function 是一種程式抽象化的功能，目地是提高程式重用性，
;; 舉例來說，一間公司的老闆不可能事必恭親，所以會把工作切出去，
;; 像是會計可能是請事務所或是找會計師進公司，不論如何，對老闆來說都不太需要全盤了解財務報表的產出方式，
;; 只需要關心給出的財務資料和回來的報表，把老闆的角色想成是程式的 main function，把會計想成是一個 Function，
;; 就會是這樣：
;;
;; 老闆 () {
;;    財報 = 會計(當月財務資料)
;; }
;;
;; 雖然經營公司不可以完全不管會計在幹麻，但程式可以（你可以會用 Function 來完成自己要做的事），
;; Clojure 呼叫 Function 的方法為：
;; (function名稱 參數1 參數2 ... 參數N)
;; 其實這就是一個 List 結構，LISP 全名叫 List Processor，設計就是以 List 處理為見長的語言，
;; Clojure 做為 LISP 的一個方言有這樣的特性很基本。
;; List 中第一個 symbol 在 eval 時會被當做 function，把後面的 symbol 做為參數傳到第一個參數所表示的的 Function 中。

(+ 1 2 3 4 5)                           ; 1 2 3 4 5 會做為參數進到 '+' 這個 function 中

;; 在 LISP （Clojure）中，這是基本規則：把第一個位置的 symbol 當做 Function，
;; 其它位置的東西的各自求值之後當做參數傳到第一個位置所表示的 Function。

;;; --------------------------------------------------------------------------------

;;; --------------------------------------------------------------------------------
;;; --------------------------------------
;;; |            資料結構                |
;;; --------------------------------------
;; Clojure 4 個基本資料結構
;;


;;; List --------------------------------------------------
;; List 是 Clojure 的基本元素，基本上 s-exp 就是 List 結構，也就是說 Clojure 程式碼和資料沒有分別，
;; 但 Clojure 要如果分辦哪一個 List 是程式 (要被 eval)，哪一個又要當成是程式中的 List 資料？
;; Clojure 中會有一個叫做 Reader 的東西，Reader 會先看 expr 中是不是有 special form，
;; 其中有一個 special form 就是叫 Quote special form，就是在左括號前加一個 single quote(')，
;; 有寫 single quote 的 list，該 list 就不會被 normal form 的規則 eval，而是回傳 list 本身。
;; 顯式表達 (literal) - Quote Special Form：
'(1 1 2 3 5 8 11)                       ; Literal

;; list function：
(list 1 1 2 3 5 8 11)                   ; Function

;;; Vector --------------------------------------------------
;; Vector 可以當做其它語言的 array，和 list 最大的不同就是能用 index 來取得資料。
[1 1 2 3 5 8 11]                        ; Literal
(vector 1 1 2 3 5 8 11)                 ; Function

(get [1 2 3] 2)
(get '(1 2 3) 2)                        ; 反觀 list 只能 get 到 nil

;; conj 的結果也和 list 不同，會將新資料放到最後面
(conj [1 2 3] 4)
(conj '(1 2 3) 4)                       ; list 加入新的資料會放到最前面 => '(4 1 2 3)

;;; Hash-Map --------------------------------------------------
;; 就像 Python 的 dict，是一組有 key-value 的資料。
{:first 1 :second 1 :third 2 :forth 3 :fifth 5 :6th 8 :7th 11}           ; Literal
{:first 1, :second 1, :third 2, :forth 3, :fifth 5, :6th 8, :7th 11}     ; Literal
{"abc" 1}                                                                ; keyword 不是必要的
(hash-map :first 1 :second 1 :third 2 :forth 3 :fifth 5 :6th 8 :7th 11)  ; Function

(get {:k 'v} :k)                        ; 可以用 get function 拿到資料
(:k {:k 'v})                            ; 用 keyword 當 function 拿資料更直覺

;; 用 first 、last、rest、butlast 等取 sub-map 的 function 回傳結果會是 vector 或 list of vector
(first {:first 1 :second 2 :third 3})   ; 回傳 vector
(butlast {:first 1 :second 2 :third 3}) ; 回傳 list of vector

;;; Set --------------------------------------------------
;; Set 是一種不能放進重覆元素的資料結構。
#{1 2 3 5 8 11}                         ; Literal (Literal 的寫法沒辦法自動濾掉重覆的 Element)
(set '(1 1 2 3 5 8 11))                 ; Function

(conj #{1 2 3} 1)                       ; set 無法加入重覆的 Element
(conj #{1 2 3} 4)                       ; 4 可以加入

;; set 比較特別的是它的排序有點怪怪的，如果想要有排序過的 set 可以用另一個 function
(sorted-set 3 5 2 1)

;;; --------------------------------------------------------------------------------



;;; --------------------------------------------------------------------------------
;;; --------------------------------------
;;; |           Special Forms            |
;;; --------------------------------------
;; 在前面有說過 LISP （Clojure）的基本規則：
;; " 把第一個位置的 symbol 當做 Function，其它位置的東西的各自求值之後當做參數傳到第一個位置所表示的 Function。 "
;; 把這個規則叫 form，而不是以上規則的叫 Special Form，Clojure 有以下 Special Form：
;; 1. def
;; 2. if
;; 3. do
;; 4. let
;; 5. quote
;; 6. fn
;; 7. loop & recur
;; 另外還有幾個這邊不會說明的：var、try、throw、monitor-enter、monitor-exit

;; 1. def
;; def 是 define 的意思，格式為：(def symbol doc-string? init?)
;; p.s. doc-string? 和 init? 後面的 '?' 表示這個參數是 0 個或 1 個。
(def gdp)

(def gdp 0)

(def gdp
  "在描述地區性生產時稱本地生產總值或地區生產總值，是一定時期內（一個季度或一年），一個區域內的經濟活動中所生產出之全部最終成果（產品和勞務）的市場價值（market value）。"
  0)

;; 2. if
;; if 的格式為 (if test then else?)，寫法如下：
(if (> gdp 64000)
  "發大財")


;; 3. do
;; (do expr*)  expr 為 s-expression 的意思，'*' 表示 0~n 個，
;; do 的作用在於，把好幾個 s-exp 組合起來，回傳最後一個 s-exp 的值：
(do
  "I can show you the world,"
  (range 100)
  (def locale (nth ["嘉義" "雲林" "台南" "高雄"] (rand-int 4)))
  (def gdp (rand-int 100000))
  (if (and (= locale "高雄")
           (> gdp 64000))
    "發大財"
    "北漂"))

;; 4. let
;; (let [bindings*] exprs*)
;; binding 的 symbol 在 let 無法 eval (會找不到該symbol)
;; example 1:

(let [var1 "value1"
       var2 "value2"]
   (str var1 " " var2))                  ; var2 和 var2 的 life cycle 只在 let 中

var1                                 ; <- var1 找不到
var2                                    ; <- var2 找不到

;; example 2:
(do
  "I can show you the world,"
  (range 101)
  (let [locale2 (nth ["嘉義" "雲林" "台南" "高雄"] (rand-int 4))
        gdp2 (rand-int 100000)]
    (if (and (= locale2 "高雄")
             (> gdp2 64000))
      "發大財"
      "北漂")))

locale2                                 ; <- local2 找不到
gdp2                                    ; <- gdp2 找不到

;; 5. quote
;; quote special form 可以讓 Clojure 區分該 List 是否要進行 normal form 的 eval 過程，
;; 如果有加 quote，list 就會回傳自己。
(* 17 (+ 8 9))                          ; <- 這個 s-exp 會被 eval

(quote (* 17 (+ 8 9)))                  ; <- 這個 s-exp 會被當成一個 List
'(* 17 (+ 8 9))                         ; <- 用 quote special form 的寫法
                                        ; p.s. quote special form 在 calva 的 eval 會失敗，請在 REPL 輸入這行看看

;; 6. fn
;; fn 其實就是 Clojure 的 lambda function，寫法為：
;;
;; (fn name? [params*] exprs*)
;; (fn name? ([params*] exprs*) +)
;; 其中 fn 的 name 不是為了給其它 s-exp 使用的名稱，它是為了做 recursive 的名稱。
;; e.g.

(fn [] "I'm no one!")

(fn fn-name [para1]
  (str "Hello, " para1 "!"))

(fn-name "peter")                       ; <- 會跳 exception
                                        ; 找不到 fn-name 這個 function

;; 用 fn 定義一個 function, 加入參數 name (命名 power)
(fn power [n e]
  (if (zero? e)
    1
    (* n (power n (dec e)))))

;; fn 外會找到名叫 "power" 的 function
;; 理由是 fn 的 name? 參數是用來給 lambda 內做 recursive 用的
(power 2 10)                            ; 找不到 power 這個 function

;; 如果想要用 lambda function，就是再加一層括號，並加上 function parameter。
;; ((fn ,,, ,,,) ,,, ,,, )
;;      ^         ^   ^
;;  1st exp      後面的 exp 會被 eval 後做為 1st exp 的 parameter
;; 第一個 s-exp 就算是 lambda function 也依然符合 LISP form 的 Eval 規則
((fn power [n e]
   (if (zero? e)
     1
     (* n (power n (dec e))))) 2 10)

;; TODO def , defn
(defn function-symbol []
  (function body))

;; Literally Lambda
;; Lambda 有一個寫法為 hash + 小刮號 ： #(exp*)
#(,,,)

;; 如果只有一個參數，'%' 用來表示該參數
(#(str "Hello, " %) "Dave")

(#(str "I'm sorry " % ", I'm afraid I can't do that.") "Dave")

;; 如果有多個參數，%N (N表示數字) 能依序代表各參數
(#(list %2 %1 %3) "🙈" "🙉" "🙊")

;; 7. loop & recur
;; 如果有學過其它語言：
;; for ( 起始變數 ; 終止條件 ; 變數變化 ) {
;; }
;;
;; (loop [起始變數]
;;    (if (終止條件)
;;      回傳值
;;      (recur 變數變化)))
;; e.g.

(loop [n 10]
  (if (< n 0)
    "BOOM!"
    (do
      (println n "!")
      (Thread/sleep 100)
      (recur (dec n)))))

((fn loop1 [n]
   (if (< n 0)
     "BOOM!"
     (do
       (println n "!")
       (Thread/sleep 100)
       (loop1 (dec n))))) 10)


;; loop 如果沒有 recur
(loop [n 10]
  (println n "!"))                      ; 結果是不會重覆執行
                                        ; 也就是說 loop 一定要和 recur 一起使用

;;; --------------------------------------------------------------------------------

;;; --------------------------------------------------------------------------------
;;; --------------------------------------
;;; |              Function              |
;;; --------------------------------------
;; 前述 fn 中的 function-name 參數並不是定義一個有名稱的 function 的方法，
;; 那 function 要如何定義呢？ 有以下兩個方法：
;; 1. def + fn
;; 2. defn

;; 1. def + fn
(def 高雄
  (fn [money]
    (repeat (quot money 1000) "貨")))

(高雄 94888)

;; 2. defn
(defn 高雄 [n]
  (repeat n "發大財"))

(高雄 10)


;;; -------------------------------------------------
;;; 常用 Function
;; 資料的判斷
;; Clojure 有一系列的 Function 是用來判斷資料型態的，
;; type 會回傳資料的類型。
;;
;; 另外還有 Predict Function，Precict Function 是一個只回傳 Boolean 的 Function，
;; Clojure 的慣例是在其結尾加 '?'，例如 nil? 就是判斷是否為 nil 的 function。
(type 123)                              ; => java.lang.Long
(type \a)                               ; => java.lang.Character
(type "string")                         ; => java.lang.String
(type nil)                              ; => nil

(double? 487)                           ; => false
(double? 2.5)                           ; => true
(nil? "nil")                            ; => false
(string? "a")
(char? "a")
(char? \a)
(integer? 9.2)
(keyword? :key)
(symbol? :key)
(symbol? 'aeohu)
(boolean? nil)

;;; -------------------------------------------------
;;; 常用 Function - 資料結構
;; 以下先用 def Function 定義 Symbol
(def my-list (range 1 11))
(def my-vector (vec (range 1 11)))
(def my-map (apply hash-map (interleave '(:first :second :third :forth :fifth :6th :7th :8th :9th :10th)
                                        (range 2 11))))
(def my-set (set (range 1 11)))

;; sub-collection 類：
;; 1. first - 取出 collection 中第一個元素
;; 2. rest - 除了 first 外的 sub-collection
;; 3. last - 取出 collection 中最後一個元素
;; 4. butlast - 除了 lest 外的 sub-collection
(first my-list)
(first my-vector)
(first my-map)
(first my-set)

(rest my-list)
(rest my-vector)
(rest my-map)
(rest my-set)

(last my-list)
(last my-vector)
(last my-map)
(last my-set)

(butlast my-list)
(butlast my-vector)
(butlast my-map)
(butlast my-set)

;; rest、butlast 的資料還是原來的型態
(list? (rest my-list))
(vector? (rest my-vector))
(map? (rest my-map))
(set? (rest my-set))

;; conj means conjoint
;; 加入新元素至 collection 中
(conj my-list 0)
(conj my-vector 0)
(conj my-map {:new 100})
(conj my-set 0)
;; set 不會重複
(conj my-set 8)

;; into
;; 把 collection 轉為另一種 collection
(into [] my-list)
(into {} [[:first 0] [:second 0]])
(into my-map [[:first 0] [:second 0]])
(into #{} '(1 1 2 3 3))

;; 回傳結果和原本不同的 collection function
;; 以下介紹的 function 回傳結果會是另一個 clojure collection - Sequence
;; Sequence 是 logical-list，但結構和 LISP 的 list 不太一樣，在 Clojure 中，
;; sequence 被用在很多核心的 function，包含 lazy functions 和 transducer，
;; 這邊只介紹一些常用的 functions，不會介紹到深入的 Sequence 功能。
;; take、take-last
;;
(take 2 my-list)
(take 2 my-vector)
(take 2 my-map)
(take 2 my-set)

(take-last 2 my-list)
(take-last 2 my-vector)
(take-last 2 my-map)
(take-last 2 my-set)

(list? (take 2 my-list))                ; 回傳不是 list
(vector? (take 2 my-vector))            ; 回傳不是 vector
(map? (take 2 my-map))                  ; 回傳不是 hash-map
(set? (take 2 my-set))                  ; 回傳不是 set
;; 取 collection 後的資料都是 seq
(seq? (take 2 my-list))
(seq? (take 2 my-vector))
(seq? (take 2 my-map))
(seq? (take 2 my-set))

;; assoc 只能用在 map 和 vector 上
(assoc [1 2 3] 0 10)     ;;=> [10 2 3]
(assoc {:key1 "old value1" :key2 "value2"}
       :key1 "value1" :key3 "value3")
;; => {:key3 "value3", :key2 "value2", :key1 "value1"}


(assoc my-vector
       0 10
       1 20
       2 30)
;;; -------------------------------------------------
;; 常用 Function - map、reduce、filter

(map (fn [x] (* x x)) '(1 2 3 4 5))

(reduce + '(1 2 3 4 5))

(filter odd? '(1 2 3 4 5 6 7 8))

;; thread
;; 下面這個寫法很醜，也很難看懂 （因為資料在內層）
(first (.split (.replace (.toUpperCase "a b c d") "A" "X") " "))

;; 改用 threading 的寫法會比較好讀
(-> "a b c d"
    .toUpperCase
    (.replace "A" "X")
    (.split " ")
    first)

;; -> 是把資料放到 function 的第一個參數，而 ->> 是放到最後一個。
;; ->>
;; ,,, 是是一個 dummy parameter，用來表示參數放的位置

(->> (range)                           ; range 會產生 lazy sequence
     (map #(* % %) ,,,)
     (filter even? ,,,)
     (take 10 ,,,)                     ; lazy sequence 用 take 可以取出真正要的資料
     (reduce +) ,,,)

;;; --------------------------------------------------------------------------------


;;; --------------------------------------------------------------------------------
;;; --------------------------------------
;;; |             Immutable              |
;;; --------------------------------------
;; 在 Clojure 中，資料是“不可變”的：

(def shopcart ["🐡" "🍕" "🍒"])

(conj shopcart "🍞")
shopcart  ; => ["🐡" "🍕" "🍒"]

;; shopcart 所表示的 vector 內容不會被更動，如果想要 shopcart 表示的內容物變更要：
(def shopcart (conj shopcart "🍞"))
shopcart  ; => ["🐡" "🍕" "🍒" "🍞"]

;;; --------------------------------------------------------------------------------
;;; --------------------------------------
;;; |            Lazy Sequence           |
;;; --------------------------------------
;; Clojure 中有很多 Function 會製造 Lazy Sequence
;; range
;;
(range)

;;; --------------------------------------------------------------------------------
;;;
;;; --------------------------------------------------------------------------------
;;; --------------------------------------
;;; |             Basic I/O              |
;;; --------------------------------------
;; 這邊只介紹兩個 Clojure 中高階的讀寫 Function：slurp 和 spit
;; 這邊個 function 不需要管檔案介面，它們會自動分辨路經再用不同底層幫使用者讀/寫資料，
;; 也就是說讀寫檔案、Socket、Http 都可以用這兩個 Function。
;; p.s. 有時候需要因應介面而加一些參數

;; spit：寫入，第一個參數為檔案路徑，支援相對路徑，根目錄預設是專案目錄，第二個參數為想寫入的資料。
(spit "io_test.txt" "Hello World")

;; slurp：讀檔，第一個參數也是檔案路徑。
(slurp "io_test.txt")


;; slurp 和 spit 也可以對其它介面操作，比如網頁：
(slurp "https://www.google.com/")

(slurp "io_test.txt")   ; <- 這個的結果是得到 403，Request 被 server 拒絕，這種情況就要用其它方法。

;;; --------------------------------------------------------------------------------
;;; --------------------------------------------------------------------------------
;;; --------------------------------------
;;; |               Lazy                 |
;;; --------------------------------------

;; Two examples of fibonacci
(do
  (def fib (lazy-cat [0 1] (map + fib (rest fib))))
  (take 90 fib))

;; example by humorless
;; fibonacci 可以用數學語言這樣子描述：
;; vector v = [1 0]^t
;; matrix A = [[1 1] [1 0]]
;; F(n) = A^n * v
;; 再取第一個 element
;; 其中 take   和  iterate 是對 A^n 建模
;;      juxt ... 是對 A 建模
(defn fibonacci [n]
  (map first
       (take n
             (iterate
              (juxt #(apply + % ) first)
              [1 0]))))

;;; 最後放上有關 Clojure 值得慘考的連結：
;; Cheatsheet - core library 的快速參考
;; https://clojure.org/api/cheatsheet
;;
;; ClojureDocs - 社群主導的文件，補足官方文件沒有範例的問題。
;; https://clojuredocs.org/
;;
;; Awesome Clojure
;; https://github.com/razum2um/awesome-clojure



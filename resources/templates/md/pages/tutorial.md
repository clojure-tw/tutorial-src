{:title "Clojure Crash Tutorial"
 :layout :page
 :klipse true
 :navbar? true
 :home? true
 }

## Clojure 和 LISP 的關係

在語法上，目前台面上看到的大部份主流語言都深受 C 影響，如 Java、JavaScript、C#、PHP 等，
而 C 的語法深受另一個更早期的語言影響 -- [ALGOL](https://zh.wikipedia.org/wiki/ALGOL)。
另外和 ALOGL 同時期，也有個語言也影響現代語言，目前語言常見的功能，像是 High Order Function、Garbage Collection、REPL、Recursion 等，
早是在 [LISP](https://zh.wikipedia.org/wiki/LISP) 中出現的。

LISP 是 LISt Processor 的簡寫，最大的特點就是它的語法：
```
(car (cdr '(1 2 3 4 5)))
```
這種語法而上面這段程式所做的行為是：
1. 把 `'(1 2 3 4 5)` 這個資料（這是一個 List）做為 `cdr` 這個 Function 輸入
2. 把 `1.` 的輸出再當成輸入到 `car` 這個 Function 中

Clojure 是一個 LISP 的方言，所以 Clojure 的語法就前一個例子那樣，只不過和 LISP 不同的是：
1. Function 名稱更現代，不會有義意不明的名稱如：car、cdr 等
2. 吸收了一些 Functional Programming 後來所發展的功能，像是 Immutable Data Structure
3. 運作在 JVM 上，所以還有和 Java 互動的功能
4. Clojure 沒有 cons cell
5. ...

由於本文主要針對連 LISP 都沒聽過的受眾，如果是 LISPer 想了解更多可以直接參考 Clojure Official 的 [Differences with other Lisps](https://clojure.org/reference/lisps)。

p.s. car 和 cdr 其實是 `contents of the address part of register number` 和 `contents of the decrement part of register number`，這其實是在說早期的電腦硬體架構

## Literal Representation Data

前述給了一個 LISP 的程式例子：
```
(car (cdr '(1 2 3 4 5)))
```
應該很容易注意到 LISP 中是直接用`空白`分隔東西的，在 LISP 中這些東西有一個專有名稱叫`atom`，是指說無法再被分割的資料，
反之 list 就是可以被分割的資料：
```
car   ; <- 這是 atom
1     ; <- 這還是 atom
(1 2) ; <- 這是 list
()    ; <- 空的 list，因為他不能被分割了，所以是 atom
```

但在 Clojure 中，atom 是另一個和 Concurrency 有關的名稱，所以 Clojure 不會稱那些`東西`為 `atom`，那我們要如果稱呼這些東西？
以官方的文件續述來看，會叫這些東西為 `Element`，中譯`元素`，這個名稱也符合較為新的語言如 JavaScript, Python 等對其資料結構內容物的稱呼，
Clojure 在很多名稱上去除了古老 LISP 的用詞。

再來讓我們看看這些`元素`，元素有型態和值，比方說 `1` 表示他是值為 `1` 的 `Integer`，Clojure 在程式語言的分類中，是屬於 `強型別` 的語言，
也就是在運算中不會任意幫使用者更動原本元素該有的型態，一定要轉型才可以，如以下例子就會出錯：
```klipse
(+ "number = " 1)
```
而以下例子則正確：
```klipse
(str "number = " 1)
```
原因是 `str` Function 有把進來的參數轉成字串。

因為 Clojure 寄身在 Java 上，在 Clojure 其實也可以用 Java Object 的方式定義資料，但目前我們只先關心 Literal Data Type，
所謂 [Literal](https://en.wikipedia.org/wiki/Literal_%28computer_programming%29)，是用特定符號表達程式中資料的寫法，
如前面的例子中 `1` 表示 Integer，`"nuber = "` 表示 String。

### Numeric
在 Clojure 中，Literal 的寫法可以表示的數字型態有：
1. Integer
2. Float Point
3. Big Number
4. Ratio

### Character, String And Regular Expression

### Symbol & Keyword

## 註解

註解不是程式碼，是為了在解式碼間記錄一些資訊，可能是程式的說明。
Clojure 的註解用 `;` Semicolon 做為開頭，在 `;`  後面的字串都不會被當做程式碼。

一般在 Clojure 的程式碼中，常常會看到 `;`、`;;`、`;;;` 這三種樣子的註解開頭，在慣例上只是為了表示三種不同階級的註解，功能上並沒有差別。
- `;` 表示單行註解
- `;;` 表示某個程式區塊的註解
- `;;;` 表示檔案的註解


## LISP FORM

在 Clojure 中，一段程式的寫法為：
```klipse
(print (str "Hello" " " "World" "!"))
```
而一組 List Collection 的資料結構為：
```klipse
'(print (str "Hello" " " "World" "!"))
```

雖然都是用小括號的寫法`(` `)`，但差異在左括號前有沒有 `Quote` (`'`)，若左括號前有 `'` 時，
該 List 不會被 `Eval`，也就是不會被當成程式碼執行，`'( <任意元素> )` 的這個寫法也可以用當成程式碼寫的方法：
```klipse
'(1 2 3 4 5)
(quote (1 2 3 4 5)) ; 這兩個寫法得到的結果相同
```

`'(<資料>*)` 是 `(quote (<資料>*))` 的簡易寫法，正式名稱為 `Quote Special Form`，是 `Special Form` 的其中一種，
而相對於 `Special Form` 的就是 `Lisp Form` 了。

左括號右邊的第一個東西會被當成 Function，第二個之後的東西會被當成參數的輸入進到 Function 中，
這是 LISP 的基本規則，也就是 LISP Form：
```klipse
(+ 1 2 3 4 5)
```

這樣寫會把 1 當成 Function，2 3 4 5 當成參數，想當然會出錯，因為 1 不是 Function
```klipse
(1 2 3 4 5)
```

LISP Form 再更精確說明，應該是 `對一個 List 求值（Eval)`，
假設有一個 List 如下：
```klipse
(* 2 3)
```
括號中，第一個元素為`*`，第二個和第三個元素為 `2` 和 `3`，當 Clojure 對這個 List 求值時會有以下步驟：
1. 確認第一個元素是否為 Function，在本例是中 `*` 是一個 function，因此進行 LISP Form 的規則。
2. 對第二個元素求值，得到 `2`
2. 對第三個元素求值，得到 `3`
3. 第二和第三個元素求值的結果作為輸入參數到第一個元素所表示的 Function
4. 整個 List 求值的結果為 `6`

也許你會有點疑惑：“為什麼一個數字 2 還需要被求值，他不是看起來就是 2 嗎？”，會有這個問題也是正常的，在其它語言中沒有這種“求值”的動作，
在 Clojure （或者說 LISP 系的語言）中會有一個求值動作的原因是，在 Compile 前還會有一個 Reader 解析 List，
Reader 看到有加 `'` 的 Symbol 或 List 才會知道不需要對他們求值。

另外，像是 `2` 這樣的基本元素 （ `2` 是 Integer），對它們求值還是會得到它們本身：
```
(eval 2) ; => 2
```
p.s. `eval` 除了表達 Evaluate 這個外，也是一個可以用的 Function。

再來一個更複雜的例子：
```klipse
(+ 1 (* 2 3) 4 5)
```
求值步驟如下：
1. 確認第一個元素是否為 Function，在本例是中 `+` 是一個 Function，因此進行 LISP Form 的規則。
2. 對第二個元素求值得到 `1`
3. 對第三個元素求值...
這時候問題來了，第三個元素是一個 List：
```
(* 2 3)
```
這時 Clojure 該怎麼做？ Clojure 會優先對這個 List 求值，完成後才會對下一個元素求值，求值的結果為 `6`。
4. 對第四個元素求值得到 `4`
5. 對第五個元素求值得到 `5`
到步驟 5. 時，原 List 變成：
```
(+ 1 6 4 5)
```

6. 元素二到五當為 Function `+` 的參數求值結果為 `16`

在這個例子不難看出，LISP Form 的 Eval 順序是 Depth-First：

![depth-first](/img/depth-first.png)

LISP Form 是 Clojure 的基本規則，LISP 的基中一個 `動作` (Verb)，
除了 LISP Form 外，在接下來還會介紹到 `Special Form` 和 `Macro`。

## List

前面已經介紹過 `LISP Form` 和 `Quote Special Form`，到此我們已經理解在 Clojure 中如果不想對資料求值，
只想表示該資料為一個 List 時，只要加上 `Quote`即可，也就是說的作用就是不求值（Delaying evaluation），
從這點不難理解為什麼 LISP 世界的人總是在說 `Code is data, data is code.`，
Data (list) 就加 `Quote`，Code (eval) 不加 `Quote`，也因為這個特性，了解怎麼操作 List 就變得相當重要。

一個操作 List 的例子：
```klipse
(eval (cons '* (rest '(+ 1 2 3 4 5))))
```

![depth-first](/img/depth-first-2.png)

`'(+ 1 2 3 4 5)` 因為加了 `'` (Quote)，因此這是一組資料 (List)，
`rest` 是我們要介紹的第一個 list 操作，功能是取出 list 中，除了第一個元素之外的其它元素，得到新的 list 回傳：
```klipse
(rest '(+ 1 2 3 4 5))
```

`cons` 也是 list 操作，功能是幫 list 加入新元素，要注意的是第一個參數是新元素，而第二個參數才是 list：
```klipse
(cons '* '(1 2 3 4 5))
```
另外要注意的是因為 list 特性是一個串一個（Linked List），新元素放到第一個成本最小，所以前例的結果才會是 `(* 1 2 3 4 5)`。

最後到了 `eval`，被 Eval 的 list 為：
```klipse
(eval '(* 1 2 3 4 5))
```

原本的 `+` 經過了 `rest` 和 `cons` 後，被轉換成 `*`，對 Clojure 來說，
程式碼可以很輕易的操作成另一個樣子，而這還不是 Clojure 最強大的特性，但到這邊我們先就此打住，優先關心 list 的操作。

### Functions of List

以下是常用的幾個 Function：
- first
- rest
- last
- butlast
- cons
- conj
- count
- reverse
- map
- filter
- reduce

`first`、`rest`、`last`、`butlast` 這四個 Function 可以視為同一組：
TODO 補圖

`cons` 和 `conj` 都是可以加入新元素的 Function，`cons` 請把它想成是 `first` 和 `rest` 的反向操作，假設原 list 為 `(1 2 3 4 5)`，
`first` 的結果為 `1`，`rest` 的結果為 `(2 3 4 5)`，`cons` 就是將 `1` 和 `(2 3 4 5)` 合併回去的方法：
``` klipse
(cons 1 '(2 3 4 5))
```

`count` 回傳 list 中有多少個元素：
``` klipse
(count '("Apple" "Orange" "Banana" "Churry"))
```

`reverse`


## Function

## Collection

### Sequencetial Collection

### Hashed Collection

## Special Form

## Lazy

## Sequences

## Macro

## Flow Control

## Side Effect

## Concurrency

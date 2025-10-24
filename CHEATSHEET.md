# Java to JavaScript DSA Cheatsheet

## 1. Arrays (Java `List<T>` / `int[]`)

In Java, you use `List<Integer> list = new ArrayList<>();`. In JS, you just use `let arr = [];`.

| Java (`List<T> list`) | JavaScript (`let arr = []`) | Notes & "Tricks" |
| :--- | :--- | :--- |
| `list.add(val);` | `arr.push(val);` | Adds to the end. `O(1)`. |
| `list.add(index, val);` | `arr.splice(index, 0, val);` | `splice` is the "do-it-all" mutating method. |
| `list.get(index);` | `arr[index];` | Simple bracket access. |
| `list.set(index, val);` | `arr[index] = val;` | Simple bracket assignment. |
| `list.remove(index);` | `arr.splice(index, 1);` | `splice(index, deleteCount)` |
| `list.size();` | `arr.length;` | **Key "Trick":** It's a property, not a method. |
| `list.isEmpty();` | `arr.length === 0;` | |
| `Collections.sort(list);` | `arr.sort((a, b) => a - b);` | **CRITICAL:** `arr.sort()` alone sorts as strings. You *must* provide `(a, b) => a - b` for ascending numeric sort. |
| `Collections.sort(list, (a,b) -> b-a);` | `arr.sort((a, b) => b - a);` | For descending numeric sort. |
| `List<T> copy = new ArrayList<>(list);` | `let copy = [...arr];` | **Key "Trick":** The spread syntax `...` is the standard way to make a shallow copy. |
| `list.subList(from, to);` | `arr.slice(from, to);` | `slice` is non-mutating (makes a copy). `splice` is mutating (changes the original). |
| `new int[size];` | `new Array(size);` | To create an empty array of a fixed size. |
| `Arrays.fill(arr, val);` | `new Array(size).fill(val);` | How to create an array pre-filled with values. |

---

## 2. Stack (Java `Stack<T>` / `Deque<T>`)

In Java, you use `Deque<T> stack = new ArrayDeque<>();`. In JS, you just use a plain Array.

| Java (`Deque<T> stack`) | JavaScript (`let stack = []`) | Notes |
| :--- | :--- | :--- |
| `stack.push(val);` | `stack.push(val);` | Identical. |
| `stack.pop();` | `stack.pop();` | Identical. Returns the popped value. |
| `stack.peek();` | `stack[stack.length - 1];` | Get the last item without removing it. |
| `stack.isEmpty();` | `stack.length === 0;` | |
| `stack.size();` | `stack.length;` | |

---

## 3. Queue (Java `Queue<T>`)

In Java, you use `Queue<T> q = new LinkedList<>();`. In JS, you also use a plain Array, but you add to the end and remove from the front.

| Java (`Queue<T> q`) | JavaScript (`let queue = []`) | Notes |
| :--- | :--- | :--- |
| `q.add(val);` or `q.offer(val);` | `queue.push(val);` | Enqueue: Add to the end. |
| `q.poll();` or `q.remove();` | `queue.shift();` | **Key "Trick":** `shift()` removes from the front. |
| `q.peek();` | `queue[0];` | Peek at the front item. |
| `q.isEmpty();` | `queue.length === 0;` | |
| `q.size();` | `queue.length;` | |

**Performance Warning:** In JavaScript, `queue.shift()` is an `O(n)` operation because it has to re-index the entire array. For 95% of LeetCode problems, this is fast enough. If you get a "Time Limit Exceeded" on a heavy BFS problem, the "trick" is to use two-pointers (a `head` index) instead of actually calling `shift()`.

---

## 4. Hash Map (Java `Map<K, V>`)

In Java, `Map<K, V> map = new HashMap<>();`. In JS, you **must** use the `Map` object: `let map = new Map();`. (Avoid using plain objects `let map = {}` for DSA, as keys are auto-coerced to strings and you can have conflicts with built-in properties like `constructor`).

| Java (`HashMap<K, V> map`) | JavaScript (`let map = new Map()`) | Notes |
| :--- | :--- | :--- |
| `map.put(key, val);` | `map.set(key, val);` | |
| `map.get(key);` | `map.get(key);` | |
| `map.containsKey(key);` | `map.has(key);` | |
| `map.remove(key);` | `map.delete(key);` | |
| `map.size();` | `map.size;` | Property, not a method. |
| `map.isEmpty();` | `map.size === 0;` | |
| `map.clear();` | `map.clear();` | |
| `map.getOrDefault(key, 0);` | `map.get(key) ?? 0;` | **Key "Trick":** Use the "nullish coalescing operator" (`??`). It returns the right side only if the left side is `null` or `undefined`. |
| `for(Map.Entry<K,V> e: map.entrySet())` | `for (let [key, value] of map)` | **Key "Trick":** This is called "destructuring" and is very common. |
| `for(K key : map.keySet())` | `for (let key of map.keys())` | |
| `for(V val : map.values())` | `for (let value of map.values())` | |

---

## 5. Hash Set (Java `Set<T>`)

In Java, `Set<T> set = new HashSet<>();`. In JS, it's almost identical: `let set = new Set();`.

| Java (`HashSet<T> set`) | JavaScript (`let set = new Set()`) | Notes |
| :--- | :--- | :--- |
| `set.add(val);` | `set.add(val);` | |
| `set.contains(val);` | `set.has(val);` | |
| `set.remove(val);` | `set.delete(val);` | |
| `set.size();` | `set.size;` | Property, not a method. |
| `set.isEmpty();` | `set.size === 0;` | |
| `set.clear();` | `set.clear();` | |
| `for(T val : set)` | `for (let val of set)` | Identical. |
| `new HashSet<>(list);` | `let set = new Set(arr);` | Convert an array to a set to find unique elements. |

---

## 6. Strings (Java `String` / `StringBuilder`)

JS strings are immutable, just like in Java. They are also array-like, which is a helpful "trick".

| Java (`String s`) | JavaScript (`const s = "..."`) | Notes & "Tricks" |
| :--- | :--- | :--- |
| `s.length();` | `s.length;` | Property, not a method. |
| `s.charAt(i);` | `s[i];` | **Key "Trick":** You can access chars with bracket notation. |
| `s.substring(from, to);` | `s.substring(from, to);` | `s.slice(from, to)` is also common and works similarly. |
| `s.split(delimiter);` | `s.split(delimiter);` | Identical. Returns an array. |
| `String.join(delimiter, arr);` | `arr.join(delimiter);` | The method is on the array, not the `String` class. |
| `new StringBuilder sb;`<br>`sb.append(c);`<br>`sb.toString();` | `let arr = [];`<br>`arr.push(c);`<br>`arr.join('');` | **Key "Trick":** The JS equivalent of a `StringBuilder` is to build an array of characters and then `.join('')` them at the end. |
| `s.toCharArray();` | `s.split('');` | The idiomatic way to get a char array. |
| `new String(charArray);` | `charArray.join('');` | |

---

## 7. Priority Queue (Java `PriorityQueue`)

JavaScript has **no** built-in Priority Queue / Heap.

This is the single biggest gap you will face. You have two options:

### The "Slow" Way (for easy/mediums)
If `N` is small, you can just use a sorted array.

* **Add:** `arr.push(val); arr.sort((a, b) => a - b);` (This is slow, `O(n log n)` per insertion).
* **Poll (min):** `arr.shift();`
* **Poll (max):** `arr.pop();`
* **Peek (min):** `arr[0];`

### The "Right" Way (for hards)
In an interview, you'd be expected to find a different solution or, in rare cases, use a 3rd-party library (which LeetCode now supports). You can find and copy/paste a valid `MinPriorityQueue` class if you're practicing.

---

## 8. Math and Utility

| Java | JavaScript | Notes |
| :--- | :--- | :--- |
| `Math.max(a, b);` | `Math.max(a, b);` | `Math.max(...arr)` (spread operator) finds max in an array. |
| `Math.min(a, b);` | `Math.min(a, b);` | `Math.min(...arr)` finds min in an array. |
| `Math.abs(n);` | `Math.abs(n);` | |
| `Math.floor(n);` | `Math.floor(n);` | |
| `Math.ceil(n);` | `Math.ceil(n);` | |
| `Integer.MAX_VALUE` | `Number.MAX_SAFE_INTEGER` | `Infinity` is also common. |
| `Integer.MIN_VALUE` | `Number.MIN_SAFE_INTEGER` | `-Infinity` is also common. |
| `Character.isDigit(c);` | `c >= '0' && c <= '9'` | JS has no built-in `isDigit`/`isLetter`. You do it manually. |
| `(int) c` | `c.charCodeAt(0);` | Get ASCII value. |
| `(char) i` | `String.fromCharCode(i);` | Get char from ASCII value. |
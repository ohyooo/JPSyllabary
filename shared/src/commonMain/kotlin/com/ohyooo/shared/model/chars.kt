package com.ohyooo.shared.model

// length 45
val romaji = arrayOf("a", "i", "u", "e", "o", "ka", "ki", "ku", "ke", "ko", "sa", "shi", "su", "se", "so", "ta", "chi", "tsu", "te", "to", "na", "ni", "nu", "ne", "no", "ha", "hi", "fu", "he", "ho", "ma", "mi", "mu", "me", "mo", "ya", "yu", "yo", "ra", "ri", "ru", "re", "ro", "wa", "wu")

// length 45
val hiragana = arrayOf("あ", "い", "う", "え", "お", "か", "き", "く", "け", "こ", "さ", "し", "す", "せ", "そ", "た", "ち", "つ", "て", "と", "な", "に", "ぬ", "ね", "の", "は", "ひ", "ふ", "へ", "ほ", "ま", "み", "む", "め", "も", "や", "ゆ", "よ", "ら", "り", "る", "れ", "ろ", "わ", "を")

// length 45
val katakana = arrayOf("ア", "イ", "ウ", "エ", "オ", "カ", "キ", "ク", "ケ", "コ", "サ", "シ", "ス", "セ", "ソ", "タ", "チ", "ッ", "テ", "ト", "ナ", "ニ", "ヌ", "ネ", "ノ", "ハ", "ヒ", "フ", "へ", "ホ", "マ", "ミ", "ム", "メ", "モ", "ヤ", "ユ", "ヨ", "ラ", "リ", "ル", "レ", "ロ", "ワ", "ヲ")

// length 50
val sonantRomaji = arrayOf(
    "ga",
    "gi",
    "gu",
    "ge",
    "go",
    "za",
    "ji",
    "zu",
    "ze",
    "zo",
    "da",
    "dji",
    "dzu",
    "de",
    "do",
    "ba",
    "bi",
    "bu",
    "be",
    "bo",
    "ga",
    "gi",
    "gu",
    "ge",
    "go",
    "za",
    "ji",
    "zu",
    "ze",
    "zo",
    "da",
    "dji",
    "dzu",
    "de",
    "do",
    "ba",
    "bi",
    "bu",
    "be",
    "bo",
    "pa",
    "pi",
    "pu",
    "pe",
    "po",
    "pa",
    "pi",
    "pu",
    "pe",
    "po"
)

// length 50
val sonant = arrayOf("が", "ぎ", "ぐ", "げ", "ご", "ざ", "じ", "ず", "ぜ", "ぞ", "だ", "ぢ", "づ", "で", "ど", "ば", "び", "ぶ", "べ", "ぼ", "ガ", "ギ", "グ", "ゲ", "ゴ", "ザ", "ジ", "ズ", "ゼ", "ゾ", "ダ", "ヂ", "ヅ", "デ", "ド", "バ", "ビ", "ブ", "ベ", "ボ", "ぱ", "ぴ", "ぷ", "ぺ", "ぽ", "パ", "ピ", "プ", "ペ", "ポ")

val normalSequence = List(45) { it }
val sonantSequence = List(50) { it }

private val hiraganaLineAa = arrayOf("あ", "え", "い", " ", "あ", "お", "う", " ", "あ", "え", "い", "う", "え", "お", "あ", "お", " ", "あ", "い", "う", "え", "お")
private val hiraganaLineKa = arrayOf("か", "け", "き", " ", "か", "こ", "く", " ", "か", "け", "き", "く", "け", "こ", "か", "こ", " ", "か", "き", "く", "け", "こ")
private val hiraganaLineSa = arrayOf("さ", "せ", "し", " ", "さ", "そ", "す", " ", "さ", "せ", "し", "す", "せ", "そ", "さ", "そ", " ", "さ", "し", "す", "せ", "そ")
private val hiraganaLineTa = arrayOf("た", "て", "ち", " ", "た", "と", "つ", " ", "た", "て", "ち", "つ", "て", "と", "た", "と", " ", "た", "ち", "つ", "て", "と")
private val hiraganaLineNa = arrayOf("な", "ね", "に", " ", "な", "の", "ぬ", " ", "な", "ね", "に", "ぬ", "ね", "の", "な", "の", " ", "な", "に", "ぬ", "ね", "の")
private val hiraganaLineHa = arrayOf("は", "へ", "ひ", " ", "は", "ほ", "ふ", " ", "は", "へ", "ひ", "ふ", "へ", "ほ", "は", "ほ", " ", "は", "ひ", "ふ", "へ", "ほ")
private val hiraganaLineMa = arrayOf("ま", "め", "み", " ", "ま", "も", "む", " ", "ま", "め", "み", "む", "め", "も", "ま", "も", " ", "ま", "み", "む", "め", "も")
private val hiraganaLineYa = arrayOf("や", "え", "い", " ", "や", "ゆ", "よ", " ", "や", "え", "い", "ゆ", "え", "よ", "や", "よ", " ", "や", "い", "ゆ", "え", "よ")
private val hiraganaLineRa = arrayOf("ら", "れ", "り", " ", "ら", "ろ", "る", " ", "ら", "れ", "り", "る", "れ", "ろ", "ら", "ろ", " ", "ら", "り", "る", "れ", "ろ")
private val hiraganaLineWa = arrayOf("わ", "え", "い", " ", "わ", "を", "う", " ", "わ", "え", "い", "う", "え", "を", "わ", "を", " ", "わ", "い", "う", "え", "を")
val hiraganaLineArray = arrayOf(hiraganaLineAa, hiraganaLineKa, hiraganaLineSa, hiraganaLineTa, hiraganaLineNa, hiraganaLineHa, hiraganaLineMa, hiraganaLineYa, hiraganaLineRa, hiraganaLineWa)

private val katakanaLineAa = arrayOf("ア", "エ", "イ", " ", "ア", "オ", "ウ", " ", "ア", "エ", "イ", "ウ", "エ", "オ", "ア", "オ", " ", "ア", "イ", "ウ", "エ", "オ")
private val katakanaLineKa = arrayOf("カ", "ケ", "キ", " ", "カ", "コ", "ク", " ", "カ", "ケ", "キ", "ク", "ケ", "コ", "カ", "コ", " ", "カ", "キ", "ク", "ケ", "コ")
private val katakanaLineSa = arrayOf("サ", "セ", "シ", " ", "サ", "ソ", "ス", " ", "サ", "セ", "シ", "ス", "セ", "ソ", "サ", "ソ", " ", "サ", "シ", "ス", "セ", "ソ")
private val katakanaLineTa = arrayOf("タ", "テ", "チ", " ", "タ", "ト", "ツ", " ", "タ", "テ", "チ", "ツ", "テ", "ト", "タ", "ト", " ", "タ", "チ", "ツ", "テ", "ト")
private val katakanaLineNa = arrayOf("ナ", "ネ", "ニ", " ", "ナ", "ノ", "ヌ", " ", "ナ", "ネ", "ニ", "ヌ", "ネ", "ノ", "ナ", "ノ", " ", "ナ", "ニ", "ヌ", "ネ", "ノ")
private val katakanaLineHa = arrayOf("ハ", "ヘ", "ヒ", " ", "ハ", "ホ", "フ", " ", "ハ", "ヘ", "ヒ", "フ", "ヘ", "ホ", "ハ", "ホ", " ", "ハ", "ヒ", "フ", "ヘ", "ホ")
private val katakanaLineMa = arrayOf("マ", "メ", "ミ", " ", "マ", "モ", "ム", " ", "マ", "メ", "ミ", "ム", "メ", "モ", "マ", "モ", " ", "マ", "ミ", "ム", "メ", "モ")
private val katakanaLineYa = arrayOf("ヤ", "エ", "イ", " ", "ヤ", "ユ", "ヨ", " ", "ヤ", "エ", "イ", "ユ", "エ", "ヨ", "ヤ", "ヨ", " ", "ヤ", "イ", "ユ", "エ", "ヨ")
private val katakanaLineRa = arrayOf("ラ", "レ", "リ", " ", "ラ", "ロ", "ル", " ", "ラ", "レ", "リ", "ル", "レ", "ロ", "ラ", "ロ", " ", "ラ", "リ", "ル", "レ", "ロ")
private val katakanaLineWa = arrayOf("ワ", "エ", "イ", " ", "ワ", "ヲ", "ウ", " ", "ワ", "エ", "イ", "ウ", "エ", "ヲ", "ワ", "ヲ", " ", "ワ", "イ", "ウ", "エ", "ヲ")

val katakanaLineArray = arrayOf(katakanaLineAa, katakanaLineKa, katakanaLineSa, katakanaLineTa, katakanaLineNa, katakanaLineHa, katakanaLineMa, katakanaLineYa, katakanaLineRa, katakanaLineWa)

private val romajiLineAa = arrayOf("a", "e", "i", " ", "a", "o", "u", " ", "a", "e", "i", "u", "e", "o", "a", "o", " ", "a", "i", "u", "e", "o")
private val romajiLineKa = arrayOf("ka", "ke", "ki", " ", "ka", "ko", "ku", " ", "ka", "ke", "ki", "ku", "ke", "ko", "ka", "ko", " ", "ka", "ki", "ku", "ke", "ko")
private val romajiLineSa = arrayOf("sa", "se", "si", " ", "sa", "so", "su", " ", "sa", "se", "si", "su", "se", "so", "sa", "so", " ", "sa", "si", "su", "se", "so")
private val romajiLineTa = arrayOf("ta", "te", "chi", " ", "ta", "to", "tsu", " ", "ta", "te", "chi", "tsu", "te", "to", "ta", "to", "ta", "chi", "tsu", "te", "to")
private val romajiLineNa = arrayOf("na", "ne", "ni", " ", "na", "no", "nu", " ", "na", "ne", "ni", "nu", "ne", "no", "na", "no", " ", "na", "ni", "nu", "ne", "no")
private val romajiLineHa = arrayOf("ha", "he", "hi", " ", "ha", "ho", "hu", " ", "ha", "he", "hi", "hu", "he", "ho", "ha", "ho", " ", "ha", "hi", "hu", "he", "ho")
private val romajiLineMa = arrayOf("ma", "me", "mi", " ", "ma", "mo", "mu", " ", "ma", "me", "mi", "mu", "me", "mo", "ma", "mo", " ", "ma", "mi", "mu", "me", "mo")
private val romajiLineYa = arrayOf("ya", "e", "i", " ", "ya", "yu", "yo", " ", "ya", "e", "i", "yu", "e", "yo", "ya", "yo", " ", "ya", "i", "yu", "e", "yo")
private val romajiLineRa = arrayOf("ra", "re", "ri", " ", "ra", "lo", "ru", " ", "ra", "re", "ri", "ru", "re", "lo", "ra", "lo", " ", "ra", "ri", "ru", "re", "lo")
private val romajiLineWa = arrayOf("wa", "e", "i", " ", "wa", "wo", "u", " ", "wa", "e", "i", "u", "e", "wo", "wa", "wo", " ", "wa", "i", "u", "e", "wo")

val romajiLineArray = arrayOf(romajiLineAa, romajiLineKa, romajiLineSa, romajiLineTa, romajiLineNa, romajiLineHa, romajiLineMa, romajiLineYa, romajiLineRa, romajiLineWa)

val twisterList = ArrayList<List<Array<String>>>().apply {
    for (i in hiraganaLineArray.indices) {
        val itemList: MutableList<Array<String>> = ArrayList()
        itemList.add(hiraganaLineArray[i])
        itemList.add(katakanaLineArray[i])
        itemList.add(romajiLineArray[i])
        this.add(itemList)
    }
}

package com.ohyooo.shared.viewmodel

import com.ohyooo.shared.generated.resources.Res
import com.ohyooo.shared.generated.resources.*
import com.ohyooo.shared.mvi.MviStore
import org.jetbrains.compose.resources.StringResource

/**
 * State rendered by the single-character training page.
 *
 * The UI reads this state only. It requests changes by dispatching [SingleIntent]
 * to [SingleStore].
 */
data class SingleUiState(
    val type: StringResource = Res.string.katakanaWithVoiceless,
    val character: String = "あ",
    val hint: String = "a",
    val isHintVisible: Boolean = false,
)

/**
 * User actions supported by the single-character page.
 */
sealed interface SingleIntent {
    /**
     * Requests a new random kana card and hides the current hint.
     */
    data object Next : SingleIntent

    /**
     * Toggles the romaji hint for the current kana card.
     */
    data object ToggleHint : SingleIntent
}

/**
 * MVI store for the single-character training page.
 *
 * It owns kana selection, hint visibility, and type labels. Composables should
 * call [dispatch] instead of changing those values locally.
 */
class SingleStore : MviStore<SingleUiState, SingleIntent>(SingleUiState()) {
    /**
     * Recently generated indexes. Increase [queueSize] to avoid showing recently
     * seen kana again within that window.
     */
    private var tvQueue = ArrayList<Int>()
    private var queueSize = 0

    /**
     * Kana-to-romaji dictionary used to generate the next training card.
     */
    private val dicts = arrayOf(
        "あ" to "a",
        "い" to "i",
        "う" to "u",
        "え" to "e",
        "お" to "o",
        "か" to "ka",
        "き" to "ki",
        "く" to "ku",
        "け" to "ke",
        "こ" to "ko",
        "さ" to "sa",
        "し" to "shi",
        "す" to "su",
        "せ" to "se",
        "そ" to "so",
        "た" to "ta",
        "ち" to "chi",
        "つ" to "tsu",
        "て" to "te",
        "と" to "to",
        "な" to "na",
        "に" to "ni",
        "ぬ" to "nu",
        "ね" to "ne",
        "の" to "no",
        "は" to "ha",
        "ひ" to "hi",
        "ふ" to "fu",
        "へ" to "he",
        "ほ" to "ho",
        "ま" to "ma",
        "み" to "mi",
        "む" to "mu",
        "め" to "me",
        "も" to "mo",
        "や" to "ya",
        "ゆ" to "yu",
        "よ" to "yo",
        "ら" to "ra",
        "り" to "ri",
        "る" to "ru",
        "れ" to "re",
        "ろ" to "ro",
        "わ" to "wa",
        "を" to "wo",
        "が" to "ga",
        "ぎ" to "gi",
        "ぐ" to "gu",
        "げ" to "ge",
        "ご" to "go",
        "ざ" to "za",
        "じ" to "zi",
        "ず" to "zu",
        "ぜ" to "ze",
        "ぞ" to "zo",
        "だ" to "da",
        "ぢ" to "zi",
        "づ" to "zu",
        "で" to "de",
        "ど" to "do",
        "ば" to "ba",
        "び" to "bi",
        "ぶ" to "bu",
        "べ" to "be",
        "ぼ" to "bo",
        "ぱ" to "pa",
        "ぴ" to "pi",
        "ぷ" to "pu",
        "ぺ" to "pe",
        "ぽ" to "po",
        "ア" to "a",
        "イ" to "i",
        "ウ" to "u",
        "エ" to "e",
        "オ" to "o",
        "カ" to "ka",
        "キ" to "ki",
        "ク" to "ku",
        "ケ" to "ke",
        "コ" to "ko",
        "サ" to "sa",
        "シ" to "shi",
        "ス" to "su",
        "セ" to "se",
        "ソ" to "so",
        "タ" to "ta",
        "チ" to "chi",
        "ッ" to "tsu",
        "テ" to "te",
        "ト" to "to",
        "ナ" to "na",
        "ニ" to "ni",
        "ヌ" to "nu",
        "ネ" to "ne",
        "ノ" to "no",
        "ハ" to "ha",
        "ヒ" to "hi",
        "フ" to "fu",
        "へ" to "he",
        "ホ" to "ho",
        "マ" to "ma",
        "ミ" to "mi",
        "ム" to "mu",
        "メ" to "me",
        "モ" to "mo",
        "ヤ" to "ya",
        "ユ" to "yu",
        "ヨ" to "yo",
        "ラ" to "ra",
        "リ" to "ri",
        "ル" to "ru",
        "レ" to "re",
        "ロ" to "ro",
        "ワ" to "wa",
        "ヲ" to "wo",
        "ガ" to "ga",
        "ギ" to "gi",
        "グ" to "gu",
        "ゲ" to "ge",
        "ゴ" to "go",
        "ザ" to "za",
        "ジ" to "zi",
        "ズ" to "zu",
        "ゼ" to "ze",
        "ゾ" to "zo",
        "ダ" to "da",
        "ヂ" to "zi",
        "ヅ" to "zu",
        "デ" to "de",
        "ド" to "do",
        "バ" to "ba",
        "ビ" to "bi",
        "ブ" to "bu",
        "ベ" to "be",
        "ボ" to "bo",
        "パ" to "pa",
        "ピ" to "pi",
        "プ" to "pu",
        "ペ" to "pe",
        "ポ" to "po",
    )
    private val count by lazy(LazyThreadSafetyMode.NONE) { dicts.size }

    /**
     * Converts a page intent into the next [SingleUiState].
     */
    override fun reduce(intent: SingleIntent) {
        when (intent) {
            SingleIntent.Next -> showNext()
            SingleIntent.ToggleHint -> setState { it.copy(isHintVisible = !it.isHintVisible) }
        }
    }

    /**
     * Selects the next model and publishes it as UI state.
     */
    private fun showNext() {
        val model = nextModel()
        setState {
            it.copy(
                type = model.title,
                character = model.kana,
                hint = model.pron,
                isHintVisible = false,
            )
        }
    }

    /**
     * Maps a dictionary index to the localized kana group label shown above the
     * character.
     */
    private fun getType(name: Int) = when (name) {
        in 0..44 -> Res.string.katakanaWithVoiceless
        in 45..89 -> Res.string.hiraganaWithVoiceless
        in 90..109 -> Res.string.katakanaWithVoicedSound
        in 110..129 -> Res.string.hiraganaWithVoicedSound
        in 130..134 -> Res.string.hiraganaWithSemiVoiced
        in 135..139 -> Res.string.katakanaWithSemivoiced
        else -> Res.string.empty
    }

    /**
     * Builds the next random training card from [dicts].
     */
    private fun nextModel(): SingleModel {
        val random = num
        val value = dicts[random]
        val kanaValue = value.first
        val pronValue = value.second
        val title = getType(random)
        return SingleModel(title, kanaValue, pronValue)
    }

    /**
     * Generates a random dictionary index.
     *
     * The queue hook is retained so the store can avoid recent repeats by raising
     * [queueSize] without changing the UI contract.
     */
    private val num: Int
        get() {
            var num: Int
            while (true) {
                num = (0 until count).random()
                if (tvQueue.size > 0) {
                    if (!tvQueue.contains(num)) {
                        tvQueue.add(num)
                        break
                    }
                } else {
                    tvQueue.add(num)
                    break
                }
            }
            if (tvQueue.size >= queueSize) {
                tvQueue.remove(0)
            }
            return num
        }
}

/**
 * Internal domain model for one generated single-character card.
 */
private data class SingleModel(
    val title: StringResource,
    val kana: String,
    val pron: String,
)

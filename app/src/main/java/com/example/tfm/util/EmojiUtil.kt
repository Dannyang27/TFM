package com.example.tfm.util

import com.example.tfm.enum.EmojiTab

object EmojiUtil {

    private val faceRaw = arrayListOf(
        "😀","😁","😂","🤣","😃","😄","😅","😆","😉","😊","😋","😎","😍","😘","🥰","😗","😙","😚","☺️",
        "🙂","🤗","🤩","🤔","🤨","😐","😑","😶","🙄","😏","😣","😥","😮","🤐","😯","😪","😫","😴","😌",
        "😛","😜","😝","🤤","😒","😓","😔","😕","🙃","🤑","😲","☹️","🙁","😖","😞","😟","😤","😢","😭",
        "😦","😧","😨","😩","🤯","😬","😰","😱","🥵","🥶","😳","🤪","😵","😡","😠","🤬","😷","🤒","🤕",
        "🤢","🤮","🤧","😇","🤠","🤡","🥳","🥴","🥺","🤥","🤫","🤭","🧐","🤓","😈","👿","👹","👺","💀",
        "👻","👽","🤖","💩","😺","😸","😹","😻","😼","😽","🙀","😿","😾","👶","👧","🧒","👦","👩","🧑",
        "👨","👵","🧓","👴","🤲","👐","🙌","👏","🤝","👍","👎", "👊","✊","🤛","🤜","🤞","✌️","🤟","🤘"
        ,"👌","👈","👉","👆","👇","☝️","✋","🤚","🖐","🖖","👋", "🤙","💪","🦵","🦶","🖕","✍️","🙏",
        "💍","💄","💋","👄","👅","👂","👃","👣","👁","👀","🧠","🦴", "🦷","🗣","👤","👥")

    private val animalRaw = arrayListOf(
        "🐶","🐱","🐭","🐹","🐰","🦊","🦝","🐻","🐼","🦘","🦡","🐨","🐯","🦁","🐮","🐷","🐽","🐸","🐵",
        "🙈","🙉","🙊","🐒","🐔","🐧","🐦","🐤","🐣","🐥","🦆","🦢","🦅","🦉","🦚","🦜","🦇","🐺","🐗",
        "🐴","🦄","🐝","🐛","🦋","🐌","🐚","🐞","🐜","🦗","🕷","🕸","🦂","🦟","🦠","🐢","🐍","🦎","🦖",
        "🦕","🐙","🦑","🦐","🦀","🐡","🐠","🐟","🐬","🐳","🐋","🦈","🐊","🐅","🐆","🦓","🦍","🐘","🦏",
        "🦛","🐪","🐫","🦙","🦒","🐃","🐂","🐄","🐎","🐖","🐏","🐑","🐐","🦌","🐕","🐩","🐈","🐓","🦃",
        "🕊","🐇","🐁","🐀","🐿","🦔","🐾","🐉","🐲","🌵","🎄","🌲","🌳","🌴","🌱","🌿","☘️","🍀","🎍",
        "🎋","🍃","🍂","🍁","🍄","🌾","💐","🌷","🌹","🥀","🌺","🌸","🌼","🌻","🌞","🌝","🌛","🌜","🌚",
        "🌕","🌖","🌗","🌘","🌑","🌒","🌓","🌔","🌙","🌎","🌍","🌏","💫","⭐️","🌟","✨","⚡️","☄️","💥",
        "🔥","🌪","🌈","☀️","🌤","⛅️","🌥","☁️","🌦","🌧","⛈","🌩","🌨","❄️","☃️","⛄️","🌬","💨","💧",
        "💦","☔️","☂️","🌊","🌫")

    private val foodRaw = arrayListOf(
        "🍏","🍎","🍐","🍊","🍋","🍌","🍉","🍇","🍓","🍈","🍒","🍑","🍍","🥭","🥥","🥝","🍅","🍆","🥑",
        "🥦","🥒","🥬","🌶","🌽","🥕","🥔","🍠","🥐","🍞","🥖","🥨","🥯","🧀","🥚","🍳","🥞","🥓","🥩",
        "🍗","🍖","🌭","🍔","🍟","🍕","🥪","🥙","🌮","🌯","🥗","🥘","🥫","🍝","🍜","🍲","🍛","🍣","🍱",
        "🥟","🍤","🍙","🍚","🍘","🍥","🥮","🥠","🍢","🍡","🍧","🍨","🍦","🥧","🍰","🎂","🍮","🍭","🍬",
        "🍫","🍿","🧂","🍩","🍪","🌰","🥜","🍯","🥛","🍼","☕️","🍵","🥤","🍶","🍺","🍻","🥂","🍷","🥃",
        "🍸","🍹","🍾","🥄","🍴","🍽","🥣","🥡","🥢")

    private val sportRaw = arrayListOf(
        "⚽️","🏀","🏈","⚾️","🥎","🏐","🏉","🎾","🥏","🎱","🏓","🏸","🥅","🏒","🏑","🥍","🏏","⛳️","🏹",
        "🎣","🥊","🥋","🎽","⛸","🥌","🛷","🛹","🎿","⛷","🏂","🏆","🥇","🥈","🥉","🏅","🎖","🏵","🎗",
        "🎫","🎟","🎪","🤹‍♀️","🎭","🎨","🎬","🎤","🎧","🎼","🎹","🥁","🎷","🎺","🎸","🎻","🎲","🧩","♟",
        "🎯","🎳","🎮","🎰")

    private val vehicleRaw = arrayListOf(
        "🚗","🚕","🚙","🚌","🚎","🏎","🚓","🚑","🚒","🚐","🚚","🚛","🚜","🛴","🚲","🛵","🏍","🚨","🚔",
        "🚍","🚘","🚖","🚡","🚠","🚟","🚃","🚋","🚞","🚝","🚄","🚅","🚈","🚂","🚆","🚇","🚊","🚉","✈️",
        "🛫","🛬","🛩","💺","🛰","🚀","🛸","🚁","🛶","⛵️","🚤","🛥","🛳","⛴","🚢","⚓️","⛽️","🚧","🚦",
        "🚥","🚏","🗺","🗿","🗽","🗼","🏰","🏯","🏟","🎡","🎢","🎠","⛲️","⛱","🏖","🏝","🏜","🌋","⛰",
        "🏔","🗻","🏕","⛺️","🏠","🏡","🏘","🏚","🏗","🏭","🏢","🏬","🏣","🏤","🏥","🏦","🏨","🏪","🏫",
        "🏩","💒","🏛","⛪️","🕌","🕍","🕋","⛩","🛤","🛣","🗾","🎑","🏞","🌅","🌄","🌠","🎇","🎆","🌇",
        "🌆","🏙","🌃","🌌","🌉","🌁"
    )

    private val ideaRaw = arrayListOf(
        "⌚️","📱","📲","💻","⌨️","🖥","🖨","🖱","🖲","🕹","🗜","💽","💾","💿","📀","📼","📷","📸","📹",
        "🎥","📽","🎞","📞","☎️","📟","📠","📺","📻","🎙","🎚","🎛","⏱","⏲","⏰","🕰","⌛️","⏳","📡",
        "🔋","🔌","💡","🔦","🕯","🗑","🛢","💸","💵","💴","💶","💷","💰","💳","🧾","💎","⚖️","🔧","🔨",
        "🛠","⛏","🔩","⚙️","⛓","🔫","💣","🔪","🗡","⚔️","🛡","🚬","⚰️","⚱️","🏺","🧭","🧱","🔮","🧿",
        "🧸","📿","💈","⚗️","🔭","🧰","🧲","🧪","🧫","🧬","🧯","🔬","🕳","💊","💉","🌡","🚽","🚰","🚿",
        "🛁","🛀","🛀🏻","🛀🏼","🛀🏽","🛀🏾","🛀🏿","🧴","🧵","🧶","🧷","🧹","🧺","🧻","🧼","🧽","🛎",
        "🔑","🗝","🚪","🛋","🛏","🛌","🖼","🛍","🧳","🛒","🎁","🎈","🎏","🎀","🎊","🎉","🧨","🎎","🏮",
        "🎐","🧧","✉️","📩","📨","📧","💌","📥","📤","📦","🏷","📪","📫","📬","📭","📮","📯","📜","📃",
        "📄","📑","📊","📈","📉","🗒","🗓","📆","📅","📇","🗃","🗳","🗄","📋","📁","📂","🗂","🗞","📰",
        "📓","📔","📒","📕","📗","📘","📙","📚","📖","🔖","🔗","📎","🖇","📐","📏","📌","📍","✂️","🖊",
        "🖋","✒️","🖌","🖍","📝","✏️","🔍","🔎","🔏","🔐","🔒","🔓")

    private val characterRaw = arrayListOf(
        "❤️","🧡","💛","💚","💙","💜","🖤","💔","❣️","💕","💞","💓","💗","💖","💘","💝","💟","🆘","❌",
        "⭕️","🛑","⛔️","📛","🚫","💯", "💢","♨️","🚷","🚯","🚳","🚱","🔞","📵","🚭","❗️","❕","❓","❔",
        "‼️","⁉️","🔅","🔆","〽️","⚠️", "🚸","🔱","⚜️","🌐","💠","Ⓜ️","🌀","💤","🏧","🚾","♿️",
        "🅿️","🚻","🚮","🎦","📶","🈁","🔣","ℹ️","🔤","🎵","🎶","➕","➖","〰️","➰","➿","🔚","🔙",
        "🔛","🔝","🔜","🔈","🔇","🔉","🔊","🔔","🔕","📣","📢","👁‍🗨",
        "💬","💭","🗯","♠️","♣️","♥️","♦️","🃏","🎴","🀄️","🕐")

    private val flagRaw = arrayListOf(
        "🏳️","🏴","🏁","🚩","🏳️‍🌈","🏴‍☠️","🇦🇫","🇦🇽","🇦🇱","🇩🇿","🇦🇸","🇦🇩","🇦🇴","🇦🇮","🇦🇶",
        "🇦🇬","🇦🇷","🇦🇲","🇦🇼","🇦🇺","🇦🇹","🇦🇿","🇧🇸","🇧🇭","🇧🇩","🇧🇧","🇧🇾","🇧🇪","🇧🇿",
        "🇧🇯","🇧🇲","🇧🇹","🇧🇴","🇧🇦","🇧🇼","🇧🇷","🇮🇴","🇻🇬","🇧🇳","🇧🇬","🇧🇫","🇧🇮","🇰🇭",
        "🇨🇲","🇨🇦","🇮🇨","🇨🇻","🇧🇶","🇰🇾","🇨🇫","🇹🇩","🇨🇱","🇨🇳","🇨🇽","🇨🇨","🇨🇴","🇰🇲",
        "🇨🇬","🇨🇩","🇨🇰","🇨🇷","🇨🇮","🇭🇷","🇨🇺","🇨🇼","🇨🇾","🇨🇿","🇩🇰","🇩🇯","🇩🇲","🇩🇴",
        "🇪🇨","🇪🇬","🇸🇻","🇬🇶","🇪🇷","🇪🇪","🇪🇹","🇪🇺","🇫🇰","🇫🇴","🇫🇯","🇫🇮","🇫🇷","🇬🇫",
        "🇵🇫","🇹🇫","🇬🇦","🇬🇲","🇬🇪","🇩🇪","🇬🇭","🇬🇮","🇬🇷","🇬🇱","🇬🇩","🇬🇵","🇬🇺","🇬🇹",
        "🇬🇬","🇬🇳","🇬🇼","🇬🇾","🇭🇹","🇭🇳","🇭🇰","🇭🇺","🇮🇸","🇮🇳","🇮🇩","🇮🇷","🇮🇶","🇮🇪",
        "🇮🇲","🇮🇱","🇮🇹","🇯🇲","🇯🇵","🎌","🇯🇪","🇯🇴","🇰🇿","🇰🇪","🇰🇮","🇽🇰","🇰🇼","🇰🇬",
        "🇱🇦","🇱🇻","🇱🇧","🇱🇸","🇱🇷","🇱🇾","🇱🇮","🇱🇹","🇱🇺","🇲🇴","🇲🇰","🇲🇬","🇲🇼","🇲🇾",
        "🇲🇻","🇲🇱","🇲🇹","🇲🇭","🇲🇶","🇲🇷","🇲🇺","🇾🇹","🇲🇽","🇫🇲","🇲🇩","🇲🇨","🇲🇳","🇲🇪",
        "🇲🇸","🇲🇦","🇲🇿","🇲🇲","🇳🇦","🇳🇷","🇳🇵","🇳🇱","🇳🇨","🇳🇿","🇳🇮","🇳🇪","🇳🇬","🇳🇺",
        "🇳🇫","🇰🇵","🇲🇵","🇳🇴","🇴🇲","🇵🇰","🇵🇼","🇵🇸","🇵🇦","🇵🇬","🇵🇾","🇵🇪","🇵🇭","🇵🇳",
        "🇵🇱","🇵🇹","🇵🇷","🇶🇦","🇷🇪","🇷🇴","🇷🇺","🇷🇼","🇼🇸","🇸🇲","🇸🇦","🇸🇳","🇷🇸","🇸🇨",
        "🇸🇱","🇸🇬","🇸🇽","🇸🇰","🇸🇮","🇬🇸","🇸🇧","🇸🇴","🇿🇦","🇰🇷","🇸🇸","🇪🇸","🇱🇰","🇧🇱",
        "🇸🇭","🇰🇳","🇱🇨","🇵🇲","🇻🇨","🇸🇩","🇸🇷","🇸🇿","🇸🇪","🇨🇭","🇸🇾","🇹🇼","🇹🇯","🇹🇿",
        "🇹🇭","🇹🇱","🇹🇬","🇹🇰","🇹🇴","🇹🇹","🇹🇳","🇹🇷","🇹🇲","🇹🇨","🇹🇻","🇻🇮","🇺🇬","🇺🇦",
        "🇦🇪","🇬🇧","🏴󠁧󠁢󠁥󠁮󠁧󠁿","🏴󠁧󠁢󠁳󠁣󠁴󠁿","🏴󠁧󠁢󠁷󠁬󠁳󠁿","🇺🇳","🇺🇸","🇺🇾","🇺🇿","🇻🇺","🇻🇦","🇻🇪","🇻🇳","🇼🇫","🇪🇭",
        "🇾🇪","🇿🇲","🇿🇼")

    private fun getEmojiMostUsed() = arrayListOf<String>()
    private fun getEmojiFaces() = faceRaw
    private fun getEmojiAnimals() = animalRaw
    private fun getFoodEmoji() = foodRaw
    private fun getSportEmoji() = sportRaw
    private fun getVehicleEmoji() = vehicleRaw
    private fun getIdeaEmoji() = ideaRaw
    private fun getCharacterEmoji() = characterRaw
    private fun getFlagEmoji() = flagRaw

    fun getEmojiByName(emojiTab: EmojiTab): ArrayList<String> = when(emojiTab){
        EmojiTab.MOST_USED ->  getEmojiMostUsed()
        EmojiTab.FACE -> getEmojiFaces()
        EmojiTab.ANIMAL -> getEmojiAnimals()
        EmojiTab.FOOD -> getFoodEmoji()
        EmojiTab.SPORT-> getSportEmoji()
        EmojiTab.VEHICLE -> getVehicleEmoji()
        EmojiTab.IDEA -> getIdeaEmoji()
        EmojiTab.CHARACTER -> getCharacterEmoji()
        EmojiTab.FLAG -> getFlagEmoji()
    }
}
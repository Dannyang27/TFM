package com.example.tfm.util

import com.example.tfm.enum.AlphabetEmojiUnicode

object EmojiUtil {

    private val emojiFaces = arrayListOf(
        0x2713, 0x2714,
        0x1F600, 0x1F601, 0x1F602, 0x1F603, 0x1F604, 0x1F605, 0x1F606, 0x1F607, 0x1F608, 0x1F609, 0x1F60A,
        0x1F60B, 0x1F60C, 0x1F60D, 0x1F60E, 0x1F60F, 0x1F610, 0x1F611, 0x1F612, 0x1F613, 0x1F614, 0x1F615,
        0x1F616, 0x1F617, 0x1F618, 0x1F619, 0x1F61A, 0x1F61B, 0x1F61C, 0x1F61D, 0x1F61E, 0x1F61F, 0x1F620,
        0x1F621, 0x1F622, 0x1F623, 0x1F624, 0x1F625, 0x1F626, 0x1F627, 0x1F628, 0x1F629, 0x1F62A, 0x1F62B,
        0x1F62C, 0x1F62D, 0x1F62E, 0x1F62F, 0x1F630, 0x1F631, 0x1F632, 0x1F633, 0x1F634, 0x1F635, 0x1F636,
        0x1F637, 0x1F638, 0x1F639, 0x1F63A, 0x1F63B, 0x1F63C, 0x1F63D, 0x1F63E, 0x1F63F, 0x1F640, 0x1F641,
        0x1F642, 0x1F643, 0x1F644, 0X263A, 0X1F4A9, 0X1F4AA, 0x1F645, 0x1F646, 0x1F647, 0x1F648, 0x1F649,
        0x1F64A, 0x1F64B, 0x1F64C, 0x1F64E, 0x1F64F, 0x1F440, 0x1F441, 0x1F442, 0x1F443, 0x1F444, 0x1F445,
        0X261D, 0x1F446, 0x1F447, 0x1F448, 0x1F449, 0x1F44A, 0x1F44B, 0x1F44C, 0x1F44D, 0x1F44E, 0x1F44F,
        0x1F450, 0x1F451, 0x1F452, 0x1F453, 0x1F454, 0x1F455, 0x1F456, 0x1F457, 0x1F458, 0x1F459, 0x1F45A,
        0x1F45B, 0x1F45C, 0x1F45D, 0x1F45E, 0x1F45F, 0x1F460, 0x1F461, 0x1F462, 0x1F463, 0x1F464, 0x1F465,
        0x1F466, 0x1F467, 0x1F468, 0x1F469, 0x1F46A, 0x1F46B, 0x1F46C, 0x1F46D, 0x1F46E, 0x1F46F, 0x1F470,
        0x1F471, 0x1F472, 0x1F473, 0x1F474, 0x1F475, 0x1F476, 0x1F477, 0x1F478, 0x1F479, 0x1F47A, 0x1F47B,
        0x1F47C, 0x1F47D, 0x1F47E, 0x1F47F, 0x1F480, 0x1F481, 0x1F482, 0x1F483, 0x1F484, 0x1F485, 0x1F486,
        0x1F487, 0x1F488, 0x1F489, 0x1F48A, 0x1F48B, 0x1F48C, 0x1F48D, 0x1F48E, 0x1F48F, 0x1F490, 0x1F491, 0x1F492
    )

    private val animals = arrayListOf(
        0x1F400, 0x1F401, 0x1F402, 0x1F403, 0x1F404, 0x1F405, 0x1F406, 0x1F407, 0x1F408, 0x1F409, 0x1F40A,
        0x1F40B, 0x1F40C, 0x1F40D, 0x1F40E, 0x1F40F, 0x1F410, 0x1F411, 0x1F412, 0x1F413, 0x1F414, 0x1F415,
        0x1F416, 0x1F417, 0x1F418, 0x1F419, 0x1F41A, 0x1F41B, 0x1F41C, 0x1F41D, 0x1F41E, 0x1F41F, 0x1F420,
        0x1F421, 0x1F422, 0x1F423, 0x1F424, 0x1F425, 0x1F426, 0x1F427, 0x1F428, 0x1F429, 0x1F42A, 0x1F42B,
        0x1F42C, 0x1F42D, 0x1F42E, 0x1F42F, 0x1F430, 0x1F431, 0x1F432, 0x1F433, 0x1F434, 0x1F435, 0x1F436,
        0x1F437, 0x1F438, 0x1F439, 0x1F43A, 0x1F43B, 0x1F43C, 0x1F43D, 0x1F43E, 0x1F332, 0x1F333, 0x1F334,
        0x1F335, 0x1F336, 0x1F337, 0x1F30D, 0x1F30E, 0x1F30F, 0x1F310, 0x1F301, 0x1F302, 0x1F303, 0x1F304,
        0x1F305, 0x1F306, 0x1F307, 0x1F308, 0x1F309, 0x1F310, 0x1F331, 0x1F332, 0x1F333, 0x1F334, 0x1F335,
        0x1F337, 0x1F338, 0x1F339, 0x1F33A, 0x1F33B, 0x1F33C, 0x1F33D, 0x1F33E, 0x1F33F, 0x1F340, 0x1F341,
        0x1F342, 0x1F343, 0x1F311, 0x1F312, 0x1F313, 0x1F314, 0x1F315, 0x1F316, 0x1F317, 0x1F318, 0x1F319,
        0x1F31A, 0x1F31B, 0x1F31C, 0x1F31D, 0x1F31E, 0x1F31F
    )

    private val foods = arrayListOf(
        0x1F345, 0x1F346, 0x1F347, 0x1F348, 0x1F349, 0x1F34A, 0x1F34B, 0x1F34C, 0x1F34D, 0x1F34E, 0x1F34F,
        0x1F350, 0x1F351, 0x1F352, 0x1F353, 0x1F354, 0x1F355, 0x1F356, 0x1F357, 0x1F358, 0x1F359, 0x1F35A,
        0x1F35B, 0x1F35C, 0x1F35D, 0x1F35E, 0x1F35F, 0x1F360, 0x1F361, 0x1F362, 0x1F363, 0x1F364, 0x1F365,
        0x1F366, 0x1F367, 0x1F368, 0x1F369, 0x1F36A, 0x1F36B, 0x1F36C, 0x1F36D, 0x1F36E, 0x1F36F, 0x1F370,
        0x1F371, 0x1F372, 0x1F373, 0x1F374, 0x1F375, 0x1F376, 0x1F377, 0x1F378, 0x1F379, 0x1F37A, 0x1F37B,
        0x1F37C, 0x1F37D, 0x1F37E, 0x1F37F
    )

    private val sports = arrayListOf(
        0x26BD, 0x26BE, 0x1F94E, 0x1F3C0, 0x1F3D0, 0x1F3C8, 0x1F3C9, 0x1F3BE, 0x1F94F, 0x1F3B3, 0x1F3CF,
        0x1F3D1, 0x1F3D2, 0x1F94D, 0x1F3D3, 0x1F3F8, 0x1F94A, 0x1F94B, 0x1F945, 0x26F3, 0x26F8, 0x1F3A3,
        0x1F3BD, 0x1F3BF, 0x1F6F7, 0x1F94C, 0x1F3AD, 0x1F5BC, 0x1F3A8, 0x1F9F5, 0x1F9F6, 0x1F93A, 0x1F3C7,
        0x26F7, 0x1F3C2, 0x1F3CC, 0x1F3C4, 0x1F6A3, 0x1F3CA, 0x26F9, 0x1F3CB, 0x1F6B4, 0x1F6B5, 0x1F938,
        0x1F93C, 0x1F93E, 0x1F939, 0x1F9D8, 0x1F396, 0x1F3C6, 0x1F3C5, 0x1F947, 0x1F948, 0x1F949, 0x1F3BC,
        0x1F3B5, 0x1F3B6, 0x1F399, 0x1F3A4, 0x1F3A7, 0x1F4FB, 0x1F3B7, 0x1F3B8, 0x1F9B9, 0x1F9BA, 0x1F9BB,
        0x1F941
    )

    private val vehicles = arrayListOf(
        0x1F680, 0x1F6F8, 0x1F681, 0x1F682, 0x1F683, 0x1F684, 0x1F685, 0x1F686, 0x1F687, 0x1F688, 0x1F689,
        0x1F68A, 0x1F68B, 0x1F68C, 0x1F68D, 0x1F68E, 0x1F68F, 0x1F690, 0x1F691, 0x1F692, 0x1F693, 0x1F694,
        0x1F695, 0x1F696, 0x1F697, 0x1F698, 0x1F699, 0x1F69A, 0x1F69B, 0x1F69C, 0x1F69D, 0x1F69E, 0x1F69F,
        0x1F6B2, 0x1F6B6, 0x1F3E0, 0x1F3E1, 0x1F3E2, 0x1F3E3, 0x1F3E4, 0x1F3E5, 0x1F3E6, 0x1F3E7, 0x1F3E8,
        0x1F3E9, 0x1F3EA, 0x1F3EB, 0x1F3EC, 0x1F3ED, 0x1F3EE, 0x1F3EF, 0x1F3F0, 0x1F5FB, 0x1F5FC, 0x1F5FD,
        0x1F5FE, 0x1F5FF
    )

    private val ideas = arrayListOf(
        0x1F4AF, 0x1F4B0, 0x1F4B1, 0x1F4B2, 0x1F4B3, 0x1F4B4, 0x1F4B5, 0x1F4B6, 0x1F4B7, 0x1F4B8, 0x1F4B9,
        0x1F4BA, 0x1F4BB, 0x1F4BC, 0x1F4BD, 0x1F4BE, 0x1F4BF, 0x1F4C0, 0x1F4C1, 0x1F4C2, 0x1F4C3, 0x1F4C4,
        0x1F4C5, 0x1F4C6, 0x1F4C7, 0x1F4C8, 0x1F4C9, 0x1F4CA, 0x1F4CB, 0x1F4CC, 0x1F4CD, 0x1F4CE, 0x1F4CF,
        0x1F4D0, 0x1F4D1, 0x1F4D2, 0x1F4D3, 0x1F4D4, 0x1F4D5, 0x1F4D6, 0x1F4D7, 0x1F4D8, 0x1F4D9, 0x1F4DA,
        0x1F4DB, 0x1F4DC, 0x1F4DD, 0x1F4DE, 0x1F4DF, 0x1F4E0, 0x1F4E1, 0x1F4E2, 0x1F4E3, 0x1F4E4, 0x1F4E5,
        0x1F4E6, 0x1F4E7, 0x1F4E8, 0x1F4E9, 0x1F4EA, 0x1F4EB, 0x1F4EC, 0x1F4ED, 0x1F4EE, 0x1F4EF, 0x1F4F0,
        0x1F4F1, 0x1F4F2, 0x1F4F3, 0x1F4F4, 0x1F4F5, 0x1F4F6, 0x1F4F7, 0x1F4F8, 0x1F4F9, 0x1F4FA, 0x1F4FB,
        0x1F4FC, 0x1F4FD, 0x1F4FE, 0x1F4FF, 0x1F503, 0x1F504, 0x1F505, 0x1F506, 0x1F507, 0x1F508, 0x1F509,
        0x1F50A, 0x1F50B, 0x1F50C, 0x1F50D, 0x1F50E, 0x1F50F, 0x1F510, 0x1F511, 0x1F512, 0x1F513, 0x1F514,
        0x1F516, 0x1F517, 0x1F525, 0x1F526, 0x1F527, 0x1F528, 0x1F529, 0x1F52A, 0x1F52B
    )

    private val characters = arrayListOf(
        0x1F493, 0x1F494, 0x1F495, 0x1F496, 0x1F497, 0x1F498, 0x1F499, 0x1F49A, 0x1F49B, 0x1F49C, 0x1F49D,
        0x1F49E, 0x1F49F, 0x1F3E7, 0x1F6AE, 0x1F6B0, 0x1267F, 0x1F6B9, 0x1F6BA, 0x1F6BB, 0x1F6BC, 0x1F6BE,
        0x1F6C2, 0x1F6C4, 0x1F6C5, 0x26A0, 0x1F6B8, 0x1F6AB, 0x1F6B3, 0x1F6AD, 0x1F6AF, 0x1F6B1, 0x1F6B7,
        0x1F4F5, 0x1F51E, 0x1F503, 0x1F504, 0x1F519, 0x1F51A, 0x1F51B, 0x1F51C, 0x1F51D, 0x1F6D0, 0x269B,
        0x1F549, 0x2721, 0x2638, 0x262F, 0x271D, 0x2626, 0x262A, 0x262E, 0x1F54E, 0x1F52F, 0x2648, 0x2649,
        0x264A, 0x264B, 0x264C, 0x264D, 0x264E, 0x264F, 0x2650, 0x2651, 0x2652, 0x26CE, 0x1F51F
    )

    private val flags = arrayListOf(
        AlphabetEmojiUnicode.A.letter.plus(AlphabetEmojiUnicode.C.letter),
        AlphabetEmojiUnicode.A.letter.plus(AlphabetEmojiUnicode.D.letter),
        AlphabetEmojiUnicode.A.letter.plus(AlphabetEmojiUnicode.E.letter),
        AlphabetEmojiUnicode.A.letter.plus(AlphabetEmojiUnicode.F.letter),
        AlphabetEmojiUnicode.A.letter.plus(AlphabetEmojiUnicode.G.letter),
        AlphabetEmojiUnicode.A.letter.plus(AlphabetEmojiUnicode.I.letter),
        AlphabetEmojiUnicode.A.letter.plus(AlphabetEmojiUnicode.L.letter),
        AlphabetEmojiUnicode.A.letter.plus(AlphabetEmojiUnicode.M.letter),
        AlphabetEmojiUnicode.A.letter.plus(AlphabetEmojiUnicode.O.letter),
        AlphabetEmojiUnicode.A.letter.plus(AlphabetEmojiUnicode.Q.letter),
        AlphabetEmojiUnicode.A.letter.plus(AlphabetEmojiUnicode.R.letter),
        AlphabetEmojiUnicode.A.letter.plus(AlphabetEmojiUnicode.S.letter),
        AlphabetEmojiUnicode.A.letter.plus(AlphabetEmojiUnicode.T.letter),
        AlphabetEmojiUnicode.A.letter.plus(AlphabetEmojiUnicode.U.letter),
        AlphabetEmojiUnicode.A.letter.plus(AlphabetEmojiUnicode.W.letter),
        AlphabetEmojiUnicode.A.letter.plus(AlphabetEmojiUnicode.X.letter),
        AlphabetEmojiUnicode.A.letter.plus(AlphabetEmojiUnicode.Z.letter),

        AlphabetEmojiUnicode.B.letter.plus(AlphabetEmojiUnicode.A.letter),
        AlphabetEmojiUnicode.B.letter.plus(AlphabetEmojiUnicode.B.letter),
        AlphabetEmojiUnicode.B.letter.plus(AlphabetEmojiUnicode.D.letter),
        AlphabetEmojiUnicode.B.letter.plus(AlphabetEmojiUnicode.E.letter),
        AlphabetEmojiUnicode.B.letter.plus(AlphabetEmojiUnicode.F.letter),
        AlphabetEmojiUnicode.B.letter.plus(AlphabetEmojiUnicode.G.letter),
        AlphabetEmojiUnicode.B.letter.plus(AlphabetEmojiUnicode.H.letter),
        AlphabetEmojiUnicode.B.letter.plus(AlphabetEmojiUnicode.I.letter),
        AlphabetEmojiUnicode.B.letter.plus(AlphabetEmojiUnicode.J.letter),
        AlphabetEmojiUnicode.B.letter.plus(AlphabetEmojiUnicode.M.letter),
        AlphabetEmojiUnicode.B.letter.plus(AlphabetEmojiUnicode.N.letter),
        AlphabetEmojiUnicode.B.letter.plus(AlphabetEmojiUnicode.O.letter),
        AlphabetEmojiUnicode.B.letter.plus(AlphabetEmojiUnicode.R.letter),
        AlphabetEmojiUnicode.B.letter.plus(AlphabetEmojiUnicode.S.letter),
        AlphabetEmojiUnicode.B.letter.plus(AlphabetEmojiUnicode.T.letter),
        AlphabetEmojiUnicode.B.letter.plus(AlphabetEmojiUnicode.V.letter),
        AlphabetEmojiUnicode.B.letter.plus(AlphabetEmojiUnicode.W.letter),
        AlphabetEmojiUnicode.B.letter.plus(AlphabetEmojiUnicode.Y.letter),
        AlphabetEmojiUnicode.B.letter.plus(AlphabetEmojiUnicode.Z.letter),

        AlphabetEmojiUnicode.C.letter.plus(AlphabetEmojiUnicode.A.letter),
        AlphabetEmojiUnicode.C.letter.plus(AlphabetEmojiUnicode.C.letter),
        AlphabetEmojiUnicode.C.letter.plus(AlphabetEmojiUnicode.D.letter),
        AlphabetEmojiUnicode.C.letter.plus(AlphabetEmojiUnicode.F.letter),
        AlphabetEmojiUnicode.C.letter.plus(AlphabetEmojiUnicode.G.letter),
        AlphabetEmojiUnicode.C.letter.plus(AlphabetEmojiUnicode.H.letter),
        AlphabetEmojiUnicode.C.letter.plus(AlphabetEmojiUnicode.I.letter),
        AlphabetEmojiUnicode.C.letter.plus(AlphabetEmojiUnicode.K.letter),
        AlphabetEmojiUnicode.C.letter.plus(AlphabetEmojiUnicode.L.letter),
        AlphabetEmojiUnicode.C.letter.plus(AlphabetEmojiUnicode.M.letter),
        AlphabetEmojiUnicode.C.letter.plus(AlphabetEmojiUnicode.N.letter),
        AlphabetEmojiUnicode.C.letter.plus(AlphabetEmojiUnicode.O.letter),
        AlphabetEmojiUnicode.C.letter.plus(AlphabetEmojiUnicode.P.letter),
        AlphabetEmojiUnicode.C.letter.plus(AlphabetEmojiUnicode.R.letter),
        AlphabetEmojiUnicode.C.letter.plus(AlphabetEmojiUnicode.U.letter),
        AlphabetEmojiUnicode.C.letter.plus(AlphabetEmojiUnicode.V.letter),
        AlphabetEmojiUnicode.C.letter.plus(AlphabetEmojiUnicode.W.letter),
        AlphabetEmojiUnicode.C.letter.plus(AlphabetEmojiUnicode.X.letter),
        AlphabetEmojiUnicode.C.letter.plus(AlphabetEmojiUnicode.Y.letter),
        AlphabetEmojiUnicode.C.letter.plus(AlphabetEmojiUnicode.Z.letter),

        AlphabetEmojiUnicode.D.letter.plus(AlphabetEmojiUnicode.E.letter),
        AlphabetEmojiUnicode.D.letter.plus(AlphabetEmojiUnicode.G.letter),
        AlphabetEmojiUnicode.D.letter.plus(AlphabetEmojiUnicode.J.letter),
        AlphabetEmojiUnicode.D.letter.plus(AlphabetEmojiUnicode.K.letter),
        AlphabetEmojiUnicode.D.letter.plus(AlphabetEmojiUnicode.M.letter),
        AlphabetEmojiUnicode.D.letter.plus(AlphabetEmojiUnicode.O.letter),
        AlphabetEmojiUnicode.D.letter.plus(AlphabetEmojiUnicode.Z.letter),

        AlphabetEmojiUnicode.E.letter.plus(AlphabetEmojiUnicode.C.letter),
        AlphabetEmojiUnicode.E.letter.plus(AlphabetEmojiUnicode.E.letter),
        AlphabetEmojiUnicode.E.letter.plus(AlphabetEmojiUnicode.G.letter),
        AlphabetEmojiUnicode.E.letter.plus(AlphabetEmojiUnicode.H.letter),
        AlphabetEmojiUnicode.E.letter.plus(AlphabetEmojiUnicode.R.letter),
        AlphabetEmojiUnicode.E.letter.plus(AlphabetEmojiUnicode.S.letter),
        AlphabetEmojiUnicode.E.letter.plus(AlphabetEmojiUnicode.T.letter),
        AlphabetEmojiUnicode.E.letter.plus(AlphabetEmojiUnicode.U.letter),

        AlphabetEmojiUnicode.F.letter.plus(AlphabetEmojiUnicode.I.letter),
        AlphabetEmojiUnicode.F.letter.plus(AlphabetEmojiUnicode.J.letter),
        AlphabetEmojiUnicode.F.letter.plus(AlphabetEmojiUnicode.K.letter),
        AlphabetEmojiUnicode.F.letter.plus(AlphabetEmojiUnicode.M.letter),
        AlphabetEmojiUnicode.F.letter.plus(AlphabetEmojiUnicode.O.letter),
        AlphabetEmojiUnicode.F.letter.plus(AlphabetEmojiUnicode.R.letter),

        AlphabetEmojiUnicode.G.letter.plus(AlphabetEmojiUnicode.A.letter),
        AlphabetEmojiUnicode.G.letter.plus(AlphabetEmojiUnicode.B.letter),
        AlphabetEmojiUnicode.G.letter.plus(AlphabetEmojiUnicode.D.letter),
        AlphabetEmojiUnicode.G.letter.plus(AlphabetEmojiUnicode.E.letter),
        AlphabetEmojiUnicode.G.letter.plus(AlphabetEmojiUnicode.F.letter),
        AlphabetEmojiUnicode.G.letter.plus(AlphabetEmojiUnicode.G.letter),
        AlphabetEmojiUnicode.G.letter.plus(AlphabetEmojiUnicode.H.letter),
        AlphabetEmojiUnicode.G.letter.plus(AlphabetEmojiUnicode.I.letter),
        AlphabetEmojiUnicode.G.letter.plus(AlphabetEmojiUnicode.L.letter),
        AlphabetEmojiUnicode.G.letter.plus(AlphabetEmojiUnicode.M.letter),
        AlphabetEmojiUnicode.G.letter.plus(AlphabetEmojiUnicode.N.letter),
        AlphabetEmojiUnicode.G.letter.plus(AlphabetEmojiUnicode.P.letter),
        AlphabetEmojiUnicode.G.letter.plus(AlphabetEmojiUnicode.Q.letter),
        AlphabetEmojiUnicode.G.letter.plus(AlphabetEmojiUnicode.R.letter),
        AlphabetEmojiUnicode.G.letter.plus(AlphabetEmojiUnicode.S.letter),
        AlphabetEmojiUnicode.G.letter.plus(AlphabetEmojiUnicode.T.letter),
        AlphabetEmojiUnicode.G.letter.plus(AlphabetEmojiUnicode.U.letter),
        AlphabetEmojiUnicode.G.letter.plus(AlphabetEmojiUnicode.W.letter),
        AlphabetEmojiUnicode.G.letter.plus(AlphabetEmojiUnicode.Y.letter),

        AlphabetEmojiUnicode.H.letter.plus(AlphabetEmojiUnicode.K.letter),
        AlphabetEmojiUnicode.H.letter.plus(AlphabetEmojiUnicode.M.letter),
        AlphabetEmojiUnicode.H.letter.plus(AlphabetEmojiUnicode.N.letter),
        AlphabetEmojiUnicode.H.letter.plus(AlphabetEmojiUnicode.R.letter),
        AlphabetEmojiUnicode.H.letter.plus(AlphabetEmojiUnicode.T.letter),
        AlphabetEmojiUnicode.H.letter.plus(AlphabetEmojiUnicode.U.letter),

        AlphabetEmojiUnicode.I.letter.plus(AlphabetEmojiUnicode.C.letter),
        AlphabetEmojiUnicode.I.letter.plus(AlphabetEmojiUnicode.D.letter),
        AlphabetEmojiUnicode.I.letter.plus(AlphabetEmojiUnicode.E.letter),
        AlphabetEmojiUnicode.I.letter.plus(AlphabetEmojiUnicode.L.letter),
        AlphabetEmojiUnicode.I.letter.plus(AlphabetEmojiUnicode.M.letter),
        AlphabetEmojiUnicode.I.letter.plus(AlphabetEmojiUnicode.N.letter),
        AlphabetEmojiUnicode.I.letter.plus(AlphabetEmojiUnicode.O.letter),
        AlphabetEmojiUnicode.I.letter.plus(AlphabetEmojiUnicode.Q.letter),
        AlphabetEmojiUnicode.I.letter.plus(AlphabetEmojiUnicode.R.letter),
        AlphabetEmojiUnicode.I.letter.plus(AlphabetEmojiUnicode.S.letter),
        AlphabetEmojiUnicode.I.letter.plus(AlphabetEmojiUnicode.T.letter),

        AlphabetEmojiUnicode.J.letter.plus(AlphabetEmojiUnicode.E.letter),
        AlphabetEmojiUnicode.J.letter.plus(AlphabetEmojiUnicode.M.letter),
        AlphabetEmojiUnicode.J.letter.plus(AlphabetEmojiUnicode.O.letter),
        AlphabetEmojiUnicode.J.letter.plus(AlphabetEmojiUnicode.P.letter),

        AlphabetEmojiUnicode.K.letter.plus(AlphabetEmojiUnicode.E.letter),
        AlphabetEmojiUnicode.K.letter.plus(AlphabetEmojiUnicode.H.letter),
        AlphabetEmojiUnicode.K.letter.plus(AlphabetEmojiUnicode.I.letter),
        AlphabetEmojiUnicode.K.letter.plus(AlphabetEmojiUnicode.M.letter),
        AlphabetEmojiUnicode.K.letter.plus(AlphabetEmojiUnicode.N.letter),
        AlphabetEmojiUnicode.K.letter.plus(AlphabetEmojiUnicode.P.letter),
        AlphabetEmojiUnicode.K.letter.plus(AlphabetEmojiUnicode.R.letter),
        AlphabetEmojiUnicode.K.letter.plus(AlphabetEmojiUnicode.W.letter),
        AlphabetEmojiUnicode.K.letter.plus(AlphabetEmojiUnicode.Y.letter),
        AlphabetEmojiUnicode.K.letter.plus(AlphabetEmojiUnicode.Z.letter),

        AlphabetEmojiUnicode.L.letter.plus(AlphabetEmojiUnicode.A.letter),
        AlphabetEmojiUnicode.L.letter.plus(AlphabetEmojiUnicode.B.letter),
        AlphabetEmojiUnicode.L.letter.plus(AlphabetEmojiUnicode.C.letter),
        AlphabetEmojiUnicode.L.letter.plus(AlphabetEmojiUnicode.I.letter),
        AlphabetEmojiUnicode.L.letter.plus(AlphabetEmojiUnicode.K.letter),
        AlphabetEmojiUnicode.L.letter.plus(AlphabetEmojiUnicode.R.letter),
        AlphabetEmojiUnicode.L.letter.plus(AlphabetEmojiUnicode.S.letter),
        AlphabetEmojiUnicode.L.letter.plus(AlphabetEmojiUnicode.T.letter),
        AlphabetEmojiUnicode.L.letter.plus(AlphabetEmojiUnicode.U.letter),
        AlphabetEmojiUnicode.L.letter.plus(AlphabetEmojiUnicode.V.letter),
        AlphabetEmojiUnicode.L.letter.plus(AlphabetEmojiUnicode.Y.letter),

        AlphabetEmojiUnicode.M.letter.plus(AlphabetEmojiUnicode.A.letter),
        AlphabetEmojiUnicode.M.letter.plus(AlphabetEmojiUnicode.C.letter),
        AlphabetEmojiUnicode.M.letter.plus(AlphabetEmojiUnicode.D.letter),
        AlphabetEmojiUnicode.M.letter.plus(AlphabetEmojiUnicode.E.letter),
        AlphabetEmojiUnicode.M.letter.plus(AlphabetEmojiUnicode.F.letter),
        AlphabetEmojiUnicode.M.letter.plus(AlphabetEmojiUnicode.G.letter),
        AlphabetEmojiUnicode.M.letter.plus(AlphabetEmojiUnicode.H.letter),
        AlphabetEmojiUnicode.M.letter.plus(AlphabetEmojiUnicode.K.letter),
        AlphabetEmojiUnicode.M.letter.plus(AlphabetEmojiUnicode.L.letter),
        AlphabetEmojiUnicode.M.letter.plus(AlphabetEmojiUnicode.M.letter),
        AlphabetEmojiUnicode.M.letter.plus(AlphabetEmojiUnicode.N.letter),
        AlphabetEmojiUnicode.M.letter.plus(AlphabetEmojiUnicode.O.letter),
        AlphabetEmojiUnicode.M.letter.plus(AlphabetEmojiUnicode.P.letter),
        AlphabetEmojiUnicode.M.letter.plus(AlphabetEmojiUnicode.R.letter),
        AlphabetEmojiUnicode.M.letter.plus(AlphabetEmojiUnicode.S.letter),
        AlphabetEmojiUnicode.M.letter.plus(AlphabetEmojiUnicode.T.letter),
        AlphabetEmojiUnicode.M.letter.plus(AlphabetEmojiUnicode.U.letter),
        AlphabetEmojiUnicode.M.letter.plus(AlphabetEmojiUnicode.V.letter),
        AlphabetEmojiUnicode.M.letter.plus(AlphabetEmojiUnicode.W.letter),
        AlphabetEmojiUnicode.M.letter.plus(AlphabetEmojiUnicode.X.letter),
        AlphabetEmojiUnicode.M.letter.plus(AlphabetEmojiUnicode.Y.letter),
        AlphabetEmojiUnicode.M.letter.plus(AlphabetEmojiUnicode.Z.letter),

        AlphabetEmojiUnicode.N.letter.plus(AlphabetEmojiUnicode.A.letter),
        AlphabetEmojiUnicode.N.letter.plus(AlphabetEmojiUnicode.C.letter),
        AlphabetEmojiUnicode.N.letter.plus(AlphabetEmojiUnicode.E.letter),
        AlphabetEmojiUnicode.N.letter.plus(AlphabetEmojiUnicode.F.letter),
        AlphabetEmojiUnicode.N.letter.plus(AlphabetEmojiUnicode.G.letter),
        AlphabetEmojiUnicode.N.letter.plus(AlphabetEmojiUnicode.I.letter),
        AlphabetEmojiUnicode.N.letter.plus(AlphabetEmojiUnicode.L.letter),
        AlphabetEmojiUnicode.N.letter.plus(AlphabetEmojiUnicode.O.letter),
        AlphabetEmojiUnicode.N.letter.plus(AlphabetEmojiUnicode.P.letter),
        AlphabetEmojiUnicode.N.letter.plus(AlphabetEmojiUnicode.R.letter),
        AlphabetEmojiUnicode.N.letter.plus(AlphabetEmojiUnicode.U.letter),
        AlphabetEmojiUnicode.N.letter.plus(AlphabetEmojiUnicode.Z.letter),

        AlphabetEmojiUnicode.O.letter.plus(AlphabetEmojiUnicode.M.letter),

        AlphabetEmojiUnicode.P.letter.plus(AlphabetEmojiUnicode.A.letter),
        AlphabetEmojiUnicode.P.letter.plus(AlphabetEmojiUnicode.E.letter),
        AlphabetEmojiUnicode.P.letter.plus(AlphabetEmojiUnicode.F.letter),
        AlphabetEmojiUnicode.P.letter.plus(AlphabetEmojiUnicode.G.letter),
        AlphabetEmojiUnicode.P.letter.plus(AlphabetEmojiUnicode.H.letter),
        AlphabetEmojiUnicode.P.letter.plus(AlphabetEmojiUnicode.K.letter),
        AlphabetEmojiUnicode.P.letter.plus(AlphabetEmojiUnicode.L.letter),
        AlphabetEmojiUnicode.P.letter.plus(AlphabetEmojiUnicode.M.letter),
        AlphabetEmojiUnicode.P.letter.plus(AlphabetEmojiUnicode.R.letter),
        AlphabetEmojiUnicode.P.letter.plus(AlphabetEmojiUnicode.S.letter),
        AlphabetEmojiUnicode.P.letter.plus(AlphabetEmojiUnicode.T.letter),
        AlphabetEmojiUnicode.P.letter.plus(AlphabetEmojiUnicode.W.letter),
        AlphabetEmojiUnicode.P.letter.plus(AlphabetEmojiUnicode.Y.letter),

        AlphabetEmojiUnicode.Q.letter.plus(AlphabetEmojiUnicode.A.letter),

        AlphabetEmojiUnicode.R.letter.plus(AlphabetEmojiUnicode.O.letter),
        AlphabetEmojiUnicode.R.letter.plus(AlphabetEmojiUnicode.S.letter),
        AlphabetEmojiUnicode.R.letter.plus(AlphabetEmojiUnicode.U.letter),
        AlphabetEmojiUnicode.R.letter.plus(AlphabetEmojiUnicode.W.letter),

        AlphabetEmojiUnicode.S.letter.plus(AlphabetEmojiUnicode.A.letter),
        AlphabetEmojiUnicode.S.letter.plus(AlphabetEmojiUnicode.B.letter),
        AlphabetEmojiUnicode.S.letter.plus(AlphabetEmojiUnicode.C.letter),
        AlphabetEmojiUnicode.S.letter.plus(AlphabetEmojiUnicode.D.letter),
        AlphabetEmojiUnicode.S.letter.plus(AlphabetEmojiUnicode.E.letter),
        AlphabetEmojiUnicode.S.letter.plus(AlphabetEmojiUnicode.G.letter),
        AlphabetEmojiUnicode.S.letter.plus(AlphabetEmojiUnicode.H.letter),
        AlphabetEmojiUnicode.S.letter.plus(AlphabetEmojiUnicode.I.letter),
        AlphabetEmojiUnicode.S.letter.plus(AlphabetEmojiUnicode.J.letter),
        AlphabetEmojiUnicode.S.letter.plus(AlphabetEmojiUnicode.K.letter),
        AlphabetEmojiUnicode.S.letter.plus(AlphabetEmojiUnicode.L.letter),
        AlphabetEmojiUnicode.S.letter.plus(AlphabetEmojiUnicode.M.letter),
        AlphabetEmojiUnicode.S.letter.plus(AlphabetEmojiUnicode.N.letter),
        AlphabetEmojiUnicode.S.letter.plus(AlphabetEmojiUnicode.O.letter),
        AlphabetEmojiUnicode.S.letter.plus(AlphabetEmojiUnicode.R.letter),
        AlphabetEmojiUnicode.S.letter.plus(AlphabetEmojiUnicode.S.letter),
        AlphabetEmojiUnicode.S.letter.plus(AlphabetEmojiUnicode.T.letter),
        AlphabetEmojiUnicode.S.letter.plus(AlphabetEmojiUnicode.V.letter),
        AlphabetEmojiUnicode.S.letter.plus(AlphabetEmojiUnicode.X.letter),
        AlphabetEmojiUnicode.S.letter.plus(AlphabetEmojiUnicode.Y.letter),
        AlphabetEmojiUnicode.S.letter.plus(AlphabetEmojiUnicode.Z.letter),

        AlphabetEmojiUnicode.T.letter.plus(AlphabetEmojiUnicode.A.letter),
        AlphabetEmojiUnicode.T.letter.plus(AlphabetEmojiUnicode.C.letter),
        AlphabetEmojiUnicode.T.letter.plus(AlphabetEmojiUnicode.D.letter),
        AlphabetEmojiUnicode.T.letter.plus(AlphabetEmojiUnicode.G.letter),
        AlphabetEmojiUnicode.T.letter.plus(AlphabetEmojiUnicode.H.letter),
        AlphabetEmojiUnicode.T.letter.plus(AlphabetEmojiUnicode.J.letter),
        AlphabetEmojiUnicode.T.letter.plus(AlphabetEmojiUnicode.K.letter),
        AlphabetEmojiUnicode.T.letter.plus(AlphabetEmojiUnicode.L.letter),
        AlphabetEmojiUnicode.T.letter.plus(AlphabetEmojiUnicode.M.letter),
        AlphabetEmojiUnicode.T.letter.plus(AlphabetEmojiUnicode.N.letter),
        AlphabetEmojiUnicode.T.letter.plus(AlphabetEmojiUnicode.O.letter),
        AlphabetEmojiUnicode.T.letter.plus(AlphabetEmojiUnicode.R.letter),
        AlphabetEmojiUnicode.T.letter.plus(AlphabetEmojiUnicode.T.letter),
        AlphabetEmojiUnicode.T.letter.plus(AlphabetEmojiUnicode.V.letter),
        AlphabetEmojiUnicode.T.letter.plus(AlphabetEmojiUnicode.W.letter),
        AlphabetEmojiUnicode.T.letter.plus(AlphabetEmojiUnicode.Z.letter),

        AlphabetEmojiUnicode.U.letter.plus(AlphabetEmojiUnicode.A.letter),
        AlphabetEmojiUnicode.U.letter.plus(AlphabetEmojiUnicode.G.letter),
        AlphabetEmojiUnicode.U.letter.plus(AlphabetEmojiUnicode.M.letter),
        AlphabetEmojiUnicode.U.letter.plus(AlphabetEmojiUnicode.N.letter),
        AlphabetEmojiUnicode.U.letter.plus(AlphabetEmojiUnicode.S.letter),
        AlphabetEmojiUnicode.U.letter.plus(AlphabetEmojiUnicode.Y.letter),
        AlphabetEmojiUnicode.U.letter.plus(AlphabetEmojiUnicode.Z.letter),

        AlphabetEmojiUnicode.V.letter.plus(AlphabetEmojiUnicode.A.letter),
        AlphabetEmojiUnicode.V.letter.plus(AlphabetEmojiUnicode.C.letter),
        AlphabetEmojiUnicode.V.letter.plus(AlphabetEmojiUnicode.E.letter),
        AlphabetEmojiUnicode.V.letter.plus(AlphabetEmojiUnicode.G.letter),
        AlphabetEmojiUnicode.V.letter.plus(AlphabetEmojiUnicode.I.letter),
        AlphabetEmojiUnicode.V.letter.plus(AlphabetEmojiUnicode.N.letter),
        AlphabetEmojiUnicode.V.letter.plus(AlphabetEmojiUnicode.U.letter),

        AlphabetEmojiUnicode.W.letter.plus(AlphabetEmojiUnicode.F.letter),
        AlphabetEmojiUnicode.W.letter.plus(AlphabetEmojiUnicode.S.letter),

        AlphabetEmojiUnicode.X.letter.plus(AlphabetEmojiUnicode.K.letter),

        AlphabetEmojiUnicode.Y.letter.plus(AlphabetEmojiUnicode.E.letter),
        AlphabetEmojiUnicode.Y.letter.plus(AlphabetEmojiUnicode.T.letter),

        AlphabetEmojiUnicode.Z.letter.plus(AlphabetEmojiUnicode.A.letter),
        AlphabetEmojiUnicode.Z.letter.plus(AlphabetEmojiUnicode.M.letter),
        AlphabetEmojiUnicode.Z.letter.plus(AlphabetEmojiUnicode.W.letter)
    )

    fun getEmojiMostUsed() = arrayListOf<Int>()

    fun getEmojiFaces() = emojiFaces

    fun getEmojiAnimals() = animals

    fun getFoodEmoji() = foods

    fun getSportEmoji() = sports

    fun getVehicleEmoji() = vehicles

    fun getIdeaEmoji() = ideas

    fun getCharacterEmoji() = characters

    fun getFlagEmoji() = flags

    fun getEmojiUnicode(unicode: Int): String{
        return String(Character.toChars(unicode))
    }
}
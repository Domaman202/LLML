package ru.DmN.llml.llvm;

@SuppressWarnings("unused")
public enum Type {
    UNKNOWN("unknown"),
    VOID("void"),
    I1(1),
    I2(2),
    I3(3),
    I4(4),
    I5(5),
    I6(6),
    I7(7),
    I8(8),
    I9(9),
    I10(10),
    I11(11),
    I12(12),
    I13(13),
    I14(14),
    I15(15),
    I16(16),
    I17(17),
    I18(18),
    I19(19),
    I20(20),
    I21(21),
    I22(22),
    I23(23),
    I24(24),
    I25(25),
    I26(26),
    I27(27),
    I28(28),
    I29(29),
    I30(30),
    I31(31),
    I32(32),
    I33(33),
    I34(34),
    I35(35),
    I36(36),
    I37(37),
    I38(38),
    I39(39),
    I40(40),
    I41(41),
    I42(42),
    I43(43),
    I44(44),
    I45(45),
    I46(46),
    I47(47),
    I48(48),
    I49(49),
    I50(50),
    I51(51),
    I52(52),
    I53(53),
    I54(54),
    I55(55),
    I56(56),
    I57(57),
    I58(58),
    I59(59),
    I60(60),
    I61(61),
    I62(62),
    I63(63),
    I64(64),
    I65(65),
    I66(66),
    I67(67),
    I68(68),
    I69(69),
    I70(70),
    I71(71),
    I72(72),
    I73(73),
    I74(74),
    I75(75),
    I76(76),
    I77(77),
    I78(78),
    I79(79),
    I80(80),
    I81(81),
    I82(82),
    I83(83),
    I84(84),
    I85(85),
    I86(86),
    I87(87),
    I88(88),
    I89(89),
    I90(90),
    I91(91),
    I92(92),
    I93(93),
    I94(94),
    I95(95),
    I96(96),
    I97(97),
    I98(98),
    I99(99),
    I100(100),
    I101(101),
    I102(102),
    I103(103),
    I104(104),
    I105(105),
    I106(106),
    I107(107),
    I108(108),
    I109(109),
    I110(110),
    I111(111),
    I112(112),
    I113(113),
    I114(114),
    I115(115),
    I116(116),
    I117(117),
    I118(118),
    I119(119),
    I120(120),
    I121(121),
    I122(122),
    I123(123),
    I124(124),
    I125(125),
    I126(126),
    I127(127),
    I128(128),
    I129(129),
    I130(130),
    I131(131),
    I132(132),
    I133(133),
    I134(134),
    I135(135),
    I136(136),
    I137(137),
    I138(138),
    I139(139),
    I140(140),
    I141(141),
    I142(142),
    I143(143),
    I144(144),
    I145(145),
    I146(146),
    I147(147),
    I148(148),
    I149(149),
    I150(150),
    I151(151),
    I152(152),
    I153(153),
    I154(154),
    I155(155),
    I156(156),
    I157(157),
    I158(158),
    I159(159),
    I160(160),
    I161(161),
    I162(162),
    I163(163),
    I164(164),
    I165(165),
    I166(166),
    I167(167),
    I168(168),
    I169(169),
    I170(170),
    I171(171),
    I172(172),
    I173(173),
    I174(174),
    I175(175),
    I176(176),
    I177(177),
    I178(178),
    I179(179),
    I180(180),
    I181(181),
    I182(182),
    I183(183),
    I184(184),
    I185(185),
    I186(186),
    I187(187),
    I188(188),
    I189(189),
    I190(190),
    I191(191),
    I192(192),
    I193(193),
    I194(194),
    I195(195),
    I196(196),
    I197(197),
    I198(198),
    I199(199),
    I200(200),
    I201(201),
    I202(202),
    I203(203),
    I204(204),
    I205(205),
    I206(206),
    I207(207),
    I208(208),
    I209(209),
    I210(210),
    I211(211),
    I212(212),
    I213(213),
    I214(214),
    I215(215),
    I216(216),
    I217(217),
    I218(218),
    I219(219),
    I220(220),
    I221(221),
    I222(222),
    I223(223),
    I224(224),
    I225(225),
    I226(226),
    I227(227),
    I228(228),
    I229(229),
    I230(230),
    I231(231),
    I232(232),
    I233(233),
    I234(234),
    I235(235),
    I236(236),
    I237(237),
    I238(238),
    I239(239),
    I240(240),
    I241(241),
    I242(242),
    I243(243),
    I244(244),
    I245(245),
    I246(246),
    I247(247),
    I248(248),
    I249(249),
    I250(250),
    I251(251),
    I252(252),
    I253(253),
    I254(254),
    I255(255),
    I256(256);


    public final String name;
    public final int bits;

    Type(String name) {
        this.name = name;
        this.bits = -1;
    }

    Type(int bits) {
        this.name = "i" + bits;
        this.bits = bits;
    }
}

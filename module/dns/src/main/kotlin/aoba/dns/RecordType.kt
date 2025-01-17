package aoba.dns

enum class RecordType(val value: String) {
    A("A"),
    CNAME("CNAME"),
    MX("MX"),
    TXT("TXT"),
    AAAA("AAAA"),
    NS("NS"),
    CAA("CAA"),
    SRV("SRV"),
    HTTPS("HTTPS"),
    SVCB("SVCB"),
    SPF("SPF")
}
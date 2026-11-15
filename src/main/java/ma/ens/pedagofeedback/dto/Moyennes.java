package ma.ens.pedagofeedback.dto;

public class Moyennes {

    private final Double clarte;
    private final Double interactivite;
    private final Double utilite;
    private final Long total;

    // Appelé par la requête @Query de FeedbackRepository (select new ...Moyennes(...))
    public Moyennes(Double clarte, Double interactivite, Double utilite, Long total) {
        this.clarte = clarte;
        this.interactivite = interactivite;
        this.utilite = utilite;
        this.total = total;
    }

    public Double getClarte() { return clarte; }
    public Double getInteractivite() { return interactivite; }
    public Double getUtilite() { return utilite; }
    public Long getTotal() { return total; }

    public int clartePct() { return pct(clarte); }
    public int interactivitePct() { return pct(interactivite); }
    public int utilitePct() { return pct(utilite); }

    private static int pct(Double note) {
        return note == null ? 0 : (int) Math.round(note * 20);
    }
}
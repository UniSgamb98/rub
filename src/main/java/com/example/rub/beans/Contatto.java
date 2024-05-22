package com.example.rub.beans;

import com.example.rub.enums.Interessamento.InteressamentoStatus;
import com.example.rub.enums.Operatori;
import com.example.rub.enums.TipoCliente;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Contatto implements Serializable {
    private String ragioneSociale;
    private String personaRiferimento;
    private String emailReferente;
    private String telefono;
    private String paese;
    private String regione;
    private String citta;
    private String indirizzo;
    private String numeroCivico;
    private String provincia;
    private String cap;
    private InteressamentoStatus interessamento;
    private TipoCliente tipoCliente;
    private String partitaIva;
    private String codiceFiscale;
    private String titolare;
    private String emailGenereica;
    private String emailCertificata;
    private int volteContattati;
    private LocalDate ultimaChiamata;
    private LocalDate prossimaChiamata;
    private String sitoWeb;
    private UUID id;
    private UUID noteId;
    private Operatori operator;
    private double coinvolgimento;
    private int checkpoint;
    private LocalDate acquisizione;

    public Contatto(){
        noteId = UUID.randomUUID();
    }

    public String getTelefono() {
        return telefono;
    }
    public int getCheckpoint() {
        return checkpoint;
    }
    public LocalDate getAcquisizione() {
        return acquisizione;
    }
    public Operatori getOperator() {
        return operator;
    }
    public String getPersonaRiferimento() {
        return personaRiferimento;
    }
    public String getCitta() {
        return citta;
    }
    public String getPaese() {
        return paese;
    }
    public String getEmailReferente() {
        return emailReferente;
    }
    public LocalDate getProssimaChiamata() {
        return prossimaChiamata;
    }
    public LocalDate getUltimaChiamata() {
        return ultimaChiamata;
    }
    public int getVolteContattati() {
        return volteContattati;
    }
    public InteressamentoStatus getInteressamento() {
        return interessamento;
    }
    public String getRagioneSociale() {
        return ragioneSociale;
    }
    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }
    public String getCap() {
        return cap;
    }
    public String getCodiceFiscale() {
        return codiceFiscale;
    }
    public String getEmailCertificata() {
        return emailCertificata;
    }
    public String getPartitaIva() {
        return partitaIva;
    }
    public String getEmailGenereica() {
        return emailGenereica;
    }
    public String getSitoWeb() {
        return sitoWeb;
    }
    public UUID getId() {
        return id;
    }
    public String getTitolare() {
        return titolare;
    }
    public String getNumeroCivico() {
        return numeroCivico;
    }
    public String getIndirizzo() {
        return indirizzo;
    }
    public String getProvincia() {
        return provincia;
    }
    public String getRegione() {
        return regione;
    }
    public UUID getNoteId() {
        return noteId;
    }
    public double getCoinvolgimento() {
        return coinvolgimento;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
    public void setNoteId(UUID noteId) {
        this.noteId = noteId;
    }
    public void setCheckpoint(int checkpoint) {
        this.checkpoint = checkpoint;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public void setPersonaRiferimento(String personaRiferimento) {
        this.personaRiferimento = personaRiferimento;
    }
    public void setPaese(String paese) {
        this.paese = paese;
    }
    public void setEmailReferente(String emailReferente) {
        this.emailReferente = emailReferente;
    }
    public void setCitta(String citta) {
        this.citta = citta;
    }
    public void setInteressamento(InteressamentoStatus interessamento) {
        this.interessamento = interessamento;
    }
    public void setAcquisizione(LocalDate acquisizione) {
        this.acquisizione = acquisizione;
    }
    public void setProssimaChiamata(LocalDate prossimaChiamata) {
        this.prossimaChiamata = prossimaChiamata;
    }
    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }
    public void setOperator(Operatori operator) {
        this.operator = operator;
    }
    public void setUltimaChiamata(LocalDate ultimaChiamata) {
        this.ultimaChiamata = ultimaChiamata;
    }
    public void incrementVolteContattati() {
        volteContattati++;
    }
    public void setCap(String cap) {
        this.cap = cap;
    }
    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }
    public void setEmailCertificata(String emailCertificata) {
        this.emailCertificata = emailCertificata;
    }
    public void setEmailGenereica(String emailGenereica) {
        this.emailGenereica = emailGenereica;
    }
    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }
    public void setSitoWeb(String sitoWeb) {
        this.sitoWeb = sitoWeb;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public void setTitolare(String titolare) {
        this.titolare = titolare;
    }
    public void setNumeroCivico(String numeroCivico) {
        this.numeroCivico = numeroCivico;
    }
    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }
    public void setRegione(String regione) {
        this.regione = regione;
    }
    public void setVolteContattati(int volteContattati) {
        this.volteContattati = volteContattati;
    }
    public void setCoinvolgimento(double coinvolgimento) {
        this.coinvolgimento = coinvolgimento;
    }

    @Override
    public String toString(){
        return "[" + ragioneSociale + "|" + personaRiferimento + "|" + paese + "|" + citta + "|" + tipoCliente + "|" + interessamento + "|" + telefono + "|" + emailReferente + "]";
    }

    public boolean compare(Contatto c){
        boolean ret = ragioneSociale.equals(c.getRagioneSociale());
        if (!personaRiferimento.equals(c.getPersonaRiferimento())) ret = false;
        if (!emailReferente.equals(c.getEmailReferente())) ret = false;
        if (!telefono.equals(c.getTelefono())) ret = false;
        if (!paese.equals(c.getPaese())) ret = false;
        if (!regione.equals(c.getRegione())) ret = false;
        if (!citta.equals(c.getCitta())) ret = false;
        if (!indirizzo.equals(c.getIndirizzo())) ret = false;
        if (!numeroCivico.equals(c.getNumeroCivico())) ret = false;
        if (!provincia.equals(c.getProvincia())) ret = false;
        if (!cap.equals(c.getCap())) ret = false;
        if (!interessamento.equals(c.getInteressamento())) ret = false;
        if (!tipoCliente.equals(c.getTipoCliente())) ret = false;
        if (!partitaIva.equals(c.getPartitaIva())) ret = false;
        if (!codiceFiscale.equals(c.getCodiceFiscale())) ret = false;
        if (!titolare.equals(c.getTitolare())) ret = false;
        if (!emailGenereica.equals(c.getEmailGenereica())) ret = false;
        if (!emailCertificata.equals(c.getEmailCertificata())) ret = false;
        if (!(volteContattati == c.getVolteContattati())) ret = false;
        if (!Objects.equals(ultimaChiamata, c.getUltimaChiamata())) ret = false;
        if (!Objects.equals(prossimaChiamata, c.getProssimaChiamata())) ret = false;
        if (!sitoWeb.equals(c.getSitoWeb())) ret = false;
        if (!noteId.equals(c.getNoteId())) ret = false;
        if (!operator.equals(c.getOperator())) ret = false;
        if (!(coinvolgimento == (c.getCoinvolgimento()))) ret = false;
        if (!(checkpoint == (c.getCheckpoint()))) ret = false;
        if (!acquisizione.equals(c.getAcquisizione())) ret = false;
        return ret;
    }
}

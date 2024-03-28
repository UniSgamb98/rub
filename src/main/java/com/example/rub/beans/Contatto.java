package com.example.rub.beans;

import com.example.rub.enums.Interessamento;
import com.example.rub.enums.TipoCliente;

import java.io.Serializable;
import java.time.LocalDate;
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
    private Interessamento interessamento;
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
    private final UUID noteId;
    private boolean rememberMe;

    public Contatto(){
        noteId = UUID.randomUUID();
    }

    public String getTelefono() {
        return telefono;
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
    public Interessamento getInteressamento() {
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
    public boolean getRememberMe(){
        return rememberMe;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
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
    public void setInteressamento(Interessamento interessamento) {
        this.interessamento = interessamento;
    }
    public void setProssimaChiamata(LocalDate prossimaChiamata) {
        this.prossimaChiamata = prossimaChiamata;
    }
    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
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
    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
    public void setVolteContattati(int volteContattati) {
        this.volteContattati = volteContattati;
    }

    @Override
    public String toString(){
        return "[" + ragioneSociale + "|" + personaRiferimento + "|" + paese + "|" + citta + "|" + tipoCliente + "|" + interessamento + "|" + telefono + "|" + emailReferente + "]";
    }
}

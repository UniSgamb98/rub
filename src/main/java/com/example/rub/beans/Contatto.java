package com.example.rub.beans;

import com.example.rub.enums.Interessamento;
import com.example.rub.enums.TipoCliente;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Contatto implements Serializable {
    private String ragioneSociale;
    private String personaRiferimento;
    private String telefono;
    private String email;
    private int volteContattati;
    private Interessamento interessamento;
    private TipoCliente tipoCliente;
    private String paese;
    private String citta;
    private Date ultimaChiamata;
    private Date prossimaChiamata;
    private UUID id;

    // TODO: private note delle chiamate;

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

    public String getEmail() {
        return email;
    }
    public String getProssimaChiamata() {
        String ret;
        if (prossimaChiamata == null){
            ret = "N/a";
        } else {
            ret = prossimaChiamata.toString();
        }
        return ret;
    }
    public String getUltimaChiamata() {
        String ret;
        if (ultimaChiamata == null){
            ret = "N/a";
        } else {
            ret = ultimaChiamata.toString();
        }
        return ret;
    }

    public int getVolteContattati() {
        return volteContattati;
    }

    public String getInteressamento() {
        return interessamento.name();
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public String getTipoCliente() {
        return tipoCliente.name();
    }

    public UUID getId() {
        return id;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public void setInteressamento(Interessamento interessamento) {
        this.interessamento = interessamento;
    }

    public void setProssimaChiamata(Date prossimaChiamata) {
        this.prossimaChiamata = prossimaChiamata;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }

    public void setUltimaChiamata(Date ultimaChiamata) {
        this.ultimaChiamata = ultimaChiamata;
    }

    public void setVolteContattati(int volteContattati) {
        this.volteContattati = volteContattati;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return "[" + ragioneSociale + "|" + personaRiferimento + "|" + paese + "|" + citta + "|" + tipoCliente + "|" + interessamento + "|" + telefono + "|" + email + "]";
    }
}

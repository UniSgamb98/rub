package com.example.rub.beans;

import com.example.rub.enums.Interessamento;
import com.example.rub.enums.TipoCliente;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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

    public Interessamento getInteressamento() {
        return interessamento;
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
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

    public String compareChanges(Contatto modifiedBean){
        String ret = "";
        if (!Objects.equals(ragioneSociale, modifiedBean.getRagioneSociale()))  ret += "0";
        if (!Objects.equals(personaRiferimento, modifiedBean.getPersonaRiferimento()))  ret += "1";
        if (!Objects.equals(telefono, modifiedBean.getTelefono()))  ret += "2";
        if (!Objects.equals(email, modifiedBean.getEmail()))  ret += "3";
        if (!Objects.equals(interessamento, modifiedBean.getInteressamento()))  ret += "4";
        if (!Objects.equals(tipoCliente, modifiedBean.getTipoCliente()))  ret += "5";
        if (!Objects.equals(paese, modifiedBean.getPaese()))  ret += "6";
        if (!Objects.equals(citta, modifiedBean.getCitta()))  ret += "7";
        return ret;
    }
}

package com.vikisoft.londrishops.helper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IfscResponce {

@SerializedName("BRANCH")
@Expose
private String bRANCH;
@SerializedName("ADDRESS")
@Expose
private String aDDRESS;
@SerializedName("CONTACT")
@Expose
private String cONTACT;
@SerializedName("CITY")
@Expose
private String cITY;
@SerializedName("DISTRICT")
@Expose
private String dISTRICT;
@SerializedName("STATE")
@Expose
private String sTATE;
@SerializedName("BANK")
@Expose
private String bANK;
@SerializedName("BANKCODE")
@Expose
private String bANKCODE;
@SerializedName("IFSC")
@Expose
private String iFSC;

public String getBRANCH() {
return bRANCH;
}

public void setBRANCH(String bRANCH) {
this.bRANCH = bRANCH;
}

public String getADDRESS() {
return aDDRESS;
}

public void setADDRESS(String aDDRESS) {
this.aDDRESS = aDDRESS;
}

public String getCONTACT() {
return cONTACT;
}

public void setCONTACT(String cONTACT) {
this.cONTACT = cONTACT;
}

public String getCITY() {
return cITY;
}

public void setCITY(String cITY) {
this.cITY = cITY;
}

public String getDISTRICT() {
return dISTRICT;
}

public void setDISTRICT(String dISTRICT) {
this.dISTRICT = dISTRICT;
}

public String getSTATE() {
return sTATE;
}

public void setSTATE(String sTATE) {
this.sTATE = sTATE;
}

public String getBANK() {
return bANK;
}

public void setBANK(String bANK) {
this.bANK = bANK;
}

public String getBANKCODE() {
return bANKCODE;
}

public void setBANKCODE(String bANKCODE) {
this.bANKCODE = bANKCODE;
}

public String getIFSC() {
return iFSC;
}

public void setIFSC(String iFSC) {
this.iFSC = iFSC;
}

}
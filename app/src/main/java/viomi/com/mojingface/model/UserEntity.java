package viomi.com.mojingface.model;

import java.io.Serializable;

/**
 * Created by Mocc on 2018/1/10
 */

public class UserEntity implements Serializable {

    private String viomiToken;
    private String viomiUserId;
    private String viomiUserCode;
    private String weChatHeadImg;

    private String viomiNikeName;
    private String viomiHeadImg;
    private String viomiAccount;

    private String miId;
    private String miAccessToken;
    private String miUserId;
    private String scanType;
    private String miExpiresIn;
    private String miMacKey;
    private String miMacAlgorithm;

    public UserEntity() {
    }

    public UserEntity(String viomiToken, String viomiUserId, String viomiUserCode, String weChatHeadImg, String viomiNikeName, String viomiHeadImg, String viomiAccount, String miId, String miAccessToken, String miUserId, String scanType, String miExpiresIn, String miMacKey, String miMacAlgorithm) {
        this.viomiToken = viomiToken;
        this.viomiUserId = viomiUserId;
        this.viomiUserCode = viomiUserCode;
        this.weChatHeadImg = weChatHeadImg;
        this.viomiNikeName = viomiNikeName;
        this.viomiHeadImg = viomiHeadImg;
        this.viomiAccount = viomiAccount;
        this.miId = miId;
        this.miAccessToken = miAccessToken;
        this.miUserId = miUserId;
        this.scanType = scanType;
        this.miExpiresIn = miExpiresIn;
        this.miMacKey = miMacKey;
        this.miMacAlgorithm = miMacAlgorithm;
    }

    public String getViomiToken() {
        return viomiToken;
    }

    public void setViomiToken(String viomiToken) {
        this.viomiToken = viomiToken;
    }

    public String getViomiUserId() {
        return viomiUserId;
    }

    public void setViomiUserId(String viomiUserId) {
        this.viomiUserId = viomiUserId;
    }

    public String getViomiUserCode() {
        return viomiUserCode;
    }

    public void setViomiUserCode(String viomiUserCode) {
        this.viomiUserCode = viomiUserCode;
    }

    public String getWeChatHeadImg() {
        return weChatHeadImg;
    }

    public void setWeChatHeadImg(String weChatHeadImg) {
        this.weChatHeadImg = weChatHeadImg;
    }

    public String getViomiNikeName() {
        return viomiNikeName;
    }

    public void setViomiNikeName(String viomiNikeName) {
        this.viomiNikeName = viomiNikeName;
    }

    public String getViomiHeadImg() {
        return viomiHeadImg;
    }

    public void setViomiHeadImg(String viomiHeadImg) {
        this.viomiHeadImg = viomiHeadImg;
    }

    public String getViomiAccount() {
        return viomiAccount;
    }

    public void setViomiAccount(String viomiAccount) {
        this.viomiAccount = viomiAccount;
    }

    public String getMiId() {
        return miId;
    }

    public void setMiId(String miId) {
        this.miId = miId;
    }

    public String getMiAccessToken() {
        return miAccessToken;
    }

    public void setMiAccessToken(String miAccessToken) {
        this.miAccessToken = miAccessToken;
    }

    public String getMiUserId() {
        return miUserId;
    }

    public void setMiUserId(String miUserId) {
        this.miUserId = miUserId;
    }

    public String getScanType() {
        return scanType;
    }

    public void setScanType(String scanType) {
        this.scanType = scanType;
    }

    public String getMiExpiresIn() {
        return miExpiresIn;
    }

    public void setMiExpiresIn(String miExpiresIn) {
        this.miExpiresIn = miExpiresIn;
    }

    public String getMiMacKey() {
        return miMacKey;
    }

    public void setMiMacKey(String miMacKey) {
        this.miMacKey = miMacKey;
    }

    public String getMiMacAlgorithm() {
        return miMacAlgorithm;
    }

    public void setMiMacAlgorithm(String miMacAlgorithm) {
        this.miMacAlgorithm = miMacAlgorithm;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "viomiToken='" + viomiToken + '\'' +
                ", viomiUserId='" + viomiUserId + '\'' +
                ", viomiUserCode='" + viomiUserCode + '\'' +
                ", weChatHeadImg='" + weChatHeadImg + '\'' +
                ", viomiNikeName='" + viomiNikeName + '\'' +
                ", viomiHeadImg='" + viomiHeadImg + '\'' +
                ", viomiAccount='" + viomiAccount + '\'' +
                ", miId='" + miId + '\'' +
                ", miAccessToken='" + miAccessToken + '\'' +
                ", miUserId='" + miUserId + '\'' +
                ", scanType='" + scanType + '\'' +
                ", miExpiresIn='" + miExpiresIn + '\'' +
                ", miMacKey='" + miMacKey + '\'' +
                ", miMacAlgorithm='" + miMacAlgorithm + '\'' +
                '}';
    }
}

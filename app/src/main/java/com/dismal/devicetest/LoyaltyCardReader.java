package com.dismal.devicetest;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcBarcode;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.util.Log;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.CopyOnWriteArrayList;

public class LoyaltyCardReader implements NfcAdapter.ReaderCallback {
    private boolean lockIsoDep = true;
    private boolean lockMifareClassic = true;
    private boolean lockMifareUltralight = true;
    private boolean lockNdef = true;
    private boolean lockNfcA = true;
    private boolean lockNfcB = true;
    private boolean lockNfcBarcode = true;
    private boolean lockNfcF = true;
    private boolean lockNfcV = true;
    private WeakReference<AccountCallback> mAccountCallback;
    private CopyOnWriteArrayList<String> results;

    public interface AccountCallback {
        void onAccountReceived(CopyOnWriteArrayList<String> copyOnWriteArrayList);
    }

    public LoyaltyCardReader(AccountCallback accountCallback) {
        this.mAccountCallback = new WeakReference<>(accountCallback);
        this.results = new CopyOnWriteArrayList<>();
    }

    public void onTagDiscovered(Tag tag) {
        String str;
        this.results.clear();
        if (tag != null) {
            for (String str2 : tag.getTechList()) {
                Log.i("LoyaltyCardReader", "onTagDiscovered: tech=" + str2);
                if (str2.equals(IsoDep.class.getName())) {
                    str = "IsoDep:" + parseIsoDep(tag);
                } else if (str2.equals(MifareClassic.class.getName())) {
                    str = "MifareClassic:" + parseMifareClassic(tag);
                } else if (str2.equals(MifareUltralight.class.getName())) {
                    str = "MifareUltralight:" + parseMifareUltralight(tag);
                } else if (str2.equals(Ndef.class.getName())) {
                    str = "Ndef:" + parseNdef(tag);
                } else if (str2.equals(NfcA.class.getName())) {
                    str = "NfcA:" + parseNfcA(tag);
                } else if (str2.equals(NfcB.class.getName())) {
                    str = "NfcB:" + parseNfcB(tag);
                } else if (str2.equals(NfcBarcode.class.getName())) {
                    str = "NfcBarcode:" + parseNfcBarcode(tag);
                } else if (str2.equals(NfcF.class.getName())) {
                    str = "NfcF:" + parseNfcF(tag);
                } else if (str2.equals(NfcV.class.getName())) {
                    str = "NfcV:" + parseNfcV(tag);
                } else {
                    str = "Unknow Tech";
                }
                this.results.add(str);
            }
            ((AccountCallback) this.mAccountCallback.get()).onAccountReceived(this.results);
        }
    }

    private synchronized String parseIsoDep(Tag tag) {
        String info = null;
        if (lockIsoDep) {
            IsoDep isoDep = IsoDep.get(tag);
            if (isoDep != null) {
                try {
                    lockIsoDep = false;
                    isoDep.connect();
                    byte[] response = isoDep.getHiLayerResponse();
                    Log.i("LoyaltyCardReader", "parseIsoDep: response=" + response);
                    
                    if (response != null) {
                        info = "HiLayerResponse is " + ByteArrayToHexString(response);
                        Log.i("LoyaltyCardReader", "parseIsoDep: info=" + info);
                    } else {
                        info = "HiLayerResponse is no data";
                    }
                } catch (Exception e) {
                    Log.i("LoyaltyCardReader", "parseIsoDep: e=" + e.toString());
                } finally {
                    try {
                        isoDep.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    lockIsoDep = true;
                }
            }
        }
        return info;
    }

    private synchronized String parseMifareClassic(Tag tag) {
        String info = null;
        if (lockMifareClassic) {
            MifareClassic mifareClassic = MifareClassic.get(tag);
            if (mifareClassic != null) {
                try {
                    lockMifareClassic = false;
                    mifareClassic.connect();
                    byte[] block = mifareClassic.readBlock(0);
                    
                    if (block != null) {
                        info = "Block[0]=" + ByteArrayToHexString(block);
                    }
                } catch (Exception e) {
                    Log.i("LoyaltyCardReader", "parseMifareClassic: e=" + e.toString());
                } finally {
                    try {
                        mifareClassic.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    lockMifareClassic = true;
                }
            }
        }
        return info;
    }

    private synchronized String parseMifareUltralight(Tag tag) {
        String info = null;
        if (lockMifareUltralight) {
            MifareUltralight mifareUltralight = MifareUltralight.get(tag);
            if (mifareUltralight != null) {
                try {
                    lockMifareUltralight = false;
                    mifareUltralight.connect();
                    byte[] pages = mifareUltralight.readPages(0);
                    
                    if (pages != null) {
                        info = "Pages[0]=" + ByteArrayToHexString(pages);
                    }
                } catch (Exception e) {
                    Log.i("LoyaltyCardReader", "parseMifareUltralight: e=" + e.toString());
                } finally {
                    try {
                        mifareUltralight.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    lockMifareUltralight = true;
                }
            }
        }
        return info;
    }

    private synchronized String parseNdef(Tag tag) {
        String info = null;
        if (lockNdef) {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                try {
                    lockNdef = false;
                    ndef.connect();
                    
                    byte[] ndefData = null;
                    if (ndef.getCachedNdefMessage() != null) {
                        ndefData = ndef.getCachedNdefMessage().toByteArray();
                    }
                    
                    if (ndefData != null) {
                        info = "CachedNdefMessage=" + ByteArrayToHexString(ndefData);
                    }
                } catch (Exception e) {
                    Log.i("LoyaltyCardReader", "parseNdef: e=" + e.toString());
                } finally {
                    try {
                        ndef.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    lockNdef = true;
                }
            }
        }
        return info;
    }

    private synchronized String parseNfcA(Tag tag) {
        String info = null;
        if (lockNfcA) {
            NfcA nfcA = NfcA.get(tag);
            if (nfcA != null) {
                try {
                    lockNfcA = false;
                    nfcA.connect();
                    byte[] atqa = nfcA.getAtqa();
                    Log.i("LoyaltyCardReader", "parseNfcA: response=" + atqa);
                    
                    if (atqa != null) {
                        info = "Atqa=" + ByteArrayToHexString(atqa);
                        Log.i("LoyaltyCardReader", "parseNfcA: info=" + info);
                    }
                } catch (Exception e) {
                    Log.i("LoyaltyCardReader", "parseNfcA: e=" + e.toString());
                } finally {
                    try {
                        nfcA.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    lockNfcA = true;
                }
            }
        }
        return info;
    }

    private synchronized String parseNfcB(Tag tag) {
        String info = null;
        if (lockNfcB) {
            NfcB nfcB = NfcB.get(tag);
            if (nfcB != null) {
                try {
                    lockNfcB = false;
                    nfcB.connect();
                    byte[] applicationData = nfcB.getApplicationData();
                    
                    if (applicationData != null) {
                        info = "ApplicationData=" + ByteArrayToHexString(applicationData);
                    }
                } catch (Exception e) {
                    Log.i("LoyaltyCardReader", "parseNfcB: e=" + e.toString());
                } finally {
                    try {
                        nfcB.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    lockNfcB = true;
                }
            }
        }
        return info;
    }

    private synchronized String parseNfcBarcode(Tag tag) {
        String info = null;
        if (lockNfcBarcode) {
            NfcBarcode nfcBarcode = NfcBarcode.get(tag);
            if (nfcBarcode != null) {
                try {
                    lockNfcBarcode = false;
                    nfcBarcode.connect();
                    byte[] barcode = nfcBarcode.getBarcode();
                    
                    if (barcode != null) {
                        info = "HiLayerResponse:" + ByteArrayToHexString(barcode);
                    }
                } catch (Exception e) {
                    Log.i("LoyaltyCardReader", "parseNfcBarcode: e=" + e.toString());
                } finally {
                    try {
                        nfcBarcode.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    lockNfcBarcode = true;
                }
            }
        }
        return info;
    }

    private synchronized String parseNfcF(Tag tag) {
        String info = null;
        if (lockNfcF) {
            NfcF nfcF = NfcF.get(tag);
            if (nfcF != null) {
                try {
                    lockNfcF = false;
                    nfcF.connect();
                    byte[] manufacturer = nfcF.getManufacturer();
                    
                    if (manufacturer != null) {
                        info = "Manufacturer=" + ByteArrayToHexString(manufacturer);
                    }
                } catch (Exception e) {
                    Log.i("LoyaltyCardReader", "parseNfcF: e=" + e.toString());
                } finally {
                    try {
                        nfcF.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    lockNfcF = true;
                }
            }
        }
        return info;
    }

    private synchronized String parseNfcV(Tag tag) {
        String info = null;
        if (lockNfcV) {
            NfcV nfcV = NfcV.get(tag);
            if (nfcV != null) {
                try {
                    lockNfcV = false;
                    nfcV.connect();
                    byte dsfId = nfcV.getDsfId();
                    
                    if (dsfId != 0) {
                        info = "DsfId=" + ByteArrayToHexString(new byte[]{dsfId});
                    }
                } catch (Exception e) {
                    Log.i("LoyaltyCardReader", "parseNfcV: e=" + e.toString());
                } finally {
                    try {
                        nfcV.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    lockNfcV = true;
                }
            }
        }
        return info;
    }

    public static String ByteArrayToHexString(byte[] bArr) {
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] cArr2 = new char[(bArr.length * 2)];
        for (int i = 0; i < bArr.length; i++) {
            int b = bArr[i] & 255;
            int i2 = i * 2;
            cArr2[i2] = cArr[b >>> 4];
            cArr2[i2 + 1] = cArr[b & 15];
        }
        return new String(cArr2);
    }
}
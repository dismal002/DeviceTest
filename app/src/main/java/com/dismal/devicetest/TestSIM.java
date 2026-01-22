package com.dismal.devicetest;

import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import com.dismal.devicetest.ui.TestConfirm;
import java.util.ArrayList;
import java.util.List;

public class TestSIM extends TestUnitActivity {
    private ArrayList<SimInfo> infos = new ArrayList<>();
    private PhoneStateListener listener;
    private PhoneStateListener listener2;
    private List<SubscriptionInfo> mSubInfoList = null;
    private SubscriptionManager mSubscriptionManager;
    /* access modifiers changed from: private */
    public String signal_strength1;
    /* access modifiers changed from: private */
    public String signal_strength2;
    private TextView sim1;
    private TextView sim2;
    private TestConfirm simConform;
    private TelephonyManager tm;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.test_sim2);
        setTitle(R.string.but_test_sim);
        this.sim1 = (TextView) findViewById(R.id.simcard1);
        this.sim2 = (TextView) findViewById(R.id.simcard2);
        this.simConform = (TestConfirm) findViewById(R.id.sim_confirm);
        this.simConform.setTestConfirm(this);
        this.tm = (TelephonyManager) getSystemService("phone");
        int simCount = this.tm.getPhoneCount();
        this.mSubscriptionManager = SubscriptionManager.from(this);
        if (this.mSubInfoList == null) {
            this.mSubInfoList = this.mSubscriptionManager.getActiveSubscriptionInfoList();
        }
        for (int i = 0; i < simCount; i++) {
            SubscriptionInfo findRecordBySlotId = findRecordBySlotId(this.mSubInfoList, i);
            if (findRecordBySlotId != null) {
                String charSequence = findRecordBySlotId.getDisplayName().toString();
                String number = findRecordBySlotId.getNumber();
                String iccId = findRecordBySlotId.getIccId();
                String charSequence2 = findRecordBySlotId.getCarrierName().toString();
                String valueOf = String.valueOf(findRecordBySlotId.getSimSlotIndex());
                Log.e("lsz", "mSimSlotIndex-->" + valueOf);
                Log.e("lsz", "carrier" + charSequence2 + ",displayName-->" + charSequence + ",phoneNum->" + number + ",iccid-->" + iccId + ",mSimSlotIndex-->" + findRecordBySlotId.getSimSlotIndex());
                this.infos.add(new SimInfo(iccId, charSequence, number, valueOf, charSequence2, ""));
                if (findRecordBySlotId.getSimSlotIndex() == 0) {
                    this.listener = new PhoneStateListener() {
                        public void onServiceStateChanged(ServiceState serviceState) {
                        }

                        public void onDataConnectionStateChanged(int i) {
                            Log.e("lsz", "state-1111->" + i);
                        }

                        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                            Log.e("lsz", "state-1111->onSignalStrengthsChanged");
                            int dbm = 0;
                            int asuLevel = 0;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                                List<android.telephony.CellSignalStrength> cellSignalStrengths = signalStrength.getCellSignalStrengths();
                                if (!cellSignalStrengths.isEmpty()) {
                                    dbm = cellSignalStrengths.get(0).getDbm();
                                    asuLevel = cellSignalStrengths.get(0).getAsuLevel();
                                }
                            }
                            if (-1 == dbm) {
                                dbm = 0;
                            }
                            if (-1 == asuLevel) {
                                asuLevel = 0;
                            }
                            String unused = TestSIM.this.signal_strength1 = "Dbm:" + dbm + " AsuLevel:" + asuLevel;
                            TestSIM.this.updateText();
                            StringBuilder sb = new StringBuilder();
                            sb.append("signalDbm1111->");
                            sb.append(TestSIM.this.signal_strength1);
                            Log.e("lsz", sb.toString());
                        }
                    };
                }
                if (findRecordBySlotId.getSimSlotIndex() == 1) {
                    this.listener2 = new PhoneStateListener() {
                        public void onServiceStateChanged(ServiceState serviceState) {
                        }

                        public void onDataConnectionStateChanged(int i) {
                            Log.e("lsz", "state-2222->" + i);
                        }

                        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                            Log.e("lsz", "state-2222->onSignalStrengthsChanged");
                            int dbm = 0;
                            int asuLevel = 0;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                                List<android.telephony.CellSignalStrength> cellSignalStrengths = signalStrength.getCellSignalStrengths();
                                if (!cellSignalStrengths.isEmpty()) {
                                    dbm = cellSignalStrengths.get(0).getDbm();
                                    asuLevel = cellSignalStrengths.get(0).getAsuLevel();
                                }
                            }
                            if (-1 == dbm) {
                                dbm = 0;
                            }
                            if (-1 == asuLevel) {
                                asuLevel = 0;
                            }
                            String unused = TestSIM.this.signal_strength2 = "Dbm:" + dbm + " AsuLevel:" + asuLevel;
                            TestSIM.this.updateText();
                            StringBuilder sb = new StringBuilder();
                            sb.append("signal_strength2->");
                            sb.append(TestSIM.this.signal_strength2);
                            Log.e("lsz", sb.toString());
                        }
                    };
                }
            } else if (i == 0) {
                this.sim1.setText(R.string.nosim1);
            } else {
                this.sim2.setText(R.string.nosim2);
            }
        }
        updateText();
    }

    /* access modifiers changed from: private */
    public void updateText() {
        for (int i = 0; i < this.infos.size(); i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(getString(R.string.simslot));
            sb.append(this.infos.get(i).getmSimSlotIndex());
            sb.append("\n");
            sb.append(getString(R.string.simdisplayname));
            sb.append(this.infos.get(i).getDisplayName());
            sb.append("\n");
            sb.append(getString(R.string.simcarrier));
            sb.append(this.infos.get(i).getCarrier());
            sb.append("\n");
            sb.append(getString(R.string.simnum));
            sb.append(this.infos.get(i).getPhoneNum());
            sb.append("\n");
            sb.append(getString(R.string.simiccid));
            sb.append(this.infos.get(i).getIccId());
            sb.append("\n");
            if (Integer.parseInt(this.infos.get(i).getmSimSlotIndex()) == 0) {
                sb.append(getString(R.string.simstrength));
                sb.append(this.signal_strength1);
                this.sim1.setText(sb.toString());
            } else {
                sb.append(getString(R.string.simstrength));
                sb.append(this.signal_strength2);
                this.sim2.setText(sb.toString());
            }
        }
    }

    public static SubscriptionInfo findRecordBySlotId(List<SubscriptionInfo> list, int i) {
        if (list == null) {
            return null;
        }
        int size = list.size();
        for (int i2 = 0; i2 < size; i2++) {
            SubscriptionInfo subscriptionInfo = list.get(i2);
            if (subscriptionInfo.getSimSlotIndex() == i) {
                return subscriptionInfo;
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        PhoneStateListener phoneStateListener = this.listener;
        if (phoneStateListener != null) {
            this.tm.listen(phoneStateListener, 321);
        }
        PhoneStateListener phoneStateListener2 = this.listener2;
        if (phoneStateListener2 != null) {
            this.tm.listen(phoneStateListener2, 321);
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        PhoneStateListener phoneStateListener = this.listener;
        if (phoneStateListener != null) {
            this.tm.listen(phoneStateListener, 0);
        }
        PhoneStateListener phoneStateListener2 = this.listener2;
        if (phoneStateListener2 != null) {
            this.tm.listen(phoneStateListener2, 0);
        }
        super.onDestroy();
    }

    class SimInfo {
        String carrier;
        String displayName;
        String iccId;
        String mSimSlotIndex;
        String phoneNum;
        String signal_strength;

        public SimInfo(String str, String str2, String str3, String str4, String str5, String str6) {
            this.iccId = str;
            this.displayName = str2;
            this.phoneNum = str3;
            this.mSimSlotIndex = str4;
            this.carrier = str5;
            this.signal_strength = str6;
        }

        public String getCarrier() {
            return this.carrier;
        }

        public String getmSimSlotIndex() {
            return this.mSimSlotIndex;
        }

        public String getIccId() {
            return this.iccId;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public String getPhoneNum() {
            return this.phoneNum;
        }
    }
}

package com.permission.gps.test.incheon.gpspermissiontest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // 許可されたパーミッションの種類を識別する番号
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    // GPSの位置情報を許可するボタン
    private Button mBtnGrantGPS;
    // 各種情報を表示するラベル
    private TextView mTvLocationPermissionState, mTvLatitude, mTVLongitude;
    // 位置情報を管理するクラス
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvLocationPermissionState = (TextView) findViewById(R.id.tvFineLocationPermissionState);
        mTvLatitude = (TextView) findViewById(R.id.tvLatitude);
        mTVLongitude = (TextView) findViewById(R.id.tvLongitude);
        mBtnGrantGPS = (Button) findViewById(R.id.btnGrantGPS);

        // ボタンクリック時の挙動を定義
        mBtnGrantGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Android 6.0以上の場合
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    // 位置情報の取得が許可されているかチェック
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        // 権限があればLocationManagerを取得
                        Toast.makeText(MainActivity.this, "位置情報の取得は既に許可されています", Toast.LENGTH_SHORT).show();
                        initLocationManger();
                    } else {
                        // なければ権限を求めるダイアログを表示
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                    }
                // Android 6.0以下の場合
                } else {
                    // インストール時点で許可されているのでチェックの必要なし
                    Toast.makeText(MainActivity.this, "位置情報の取得は既に許可されています(Android 5.0以下です)", Toast.LENGTH_SHORT).show();
                    initLocationManger();
                }
            }
        });
    }

    // 許可を求めるダイアログでクリックした後に呼ばれる
    // どの種類の権限でもここを通るので位置情報取得の権限なのかどうかをrequestCodeで判別
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // GPSの権限を求めるコード
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // 許可されたら
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // テキストを表示してLocationManagerを取得
                mTvLocationPermissionState.setText("位置情報取得許可済み");
                Toast.makeText(this, "位置情報取得が許可されました", Toast.LENGTH_SHORT).show();
                initLocationManger();
            // 許可されなかったら
            } else {
                // 何もしない
                Toast.makeText(this, "位置情報取得が拒否されました", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // LocationManagerを取得するオリジナルの関数
    private void initLocationManger() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                3000, // 位置情報更新を行う最低更新時間間隔（ms）
                10, // 位置情報更新を行う最小距離間隔（メートル）
                new LocationListener() {
                    // ロケーションが変更された時の動き
                    @Override
                    public void onLocationChanged(Location location) {
                        // 権限のチェック
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                            return;
                        // テキストとログに位置情報を表示
                        Toast.makeText(MainActivity.this, "位置情報が更新されました", Toast.LENGTH_SHORT).show();
                        String strLatitude = String.valueOf(location.getLatitude());
                        String strLongitude = String.valueOf(location.getLongitude());
                        mTvLatitude.setText(strLatitude);
                        mTVLongitude.setText(strLongitude);
                        Log.d("GPS", strLatitude + "," + strLongitude);

                        mLocationManager.removeUpdates(this);
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Toast.makeText(MainActivity.this, "GPS provide is disabled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        Toast.makeText(MainActivity.this, "GPS provide is enabled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        Toast.makeText(MainActivity.this, "GPS provide status changed", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}

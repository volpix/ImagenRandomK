package com.example.imagenrandomk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {
    SensorManager sensorManager; // variable para acceder al sensor del dispositivo
    Sensor sensor; //variable para representar nuestro sensor
    SensorEventListener sensorEventListener; //evento para avisar cuando se mueva el dispositivo
    int mov=0;

    ImageView imagen;
    TextView letrero;
    Button btn;

    int lu,templu=0;
    String lux,templux=null;
    MediaPlayer music;
    Vibrator vibrator;
    int sr = 0;

    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE); // instanciamos la variable sensorManager
        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); //especificar que tipo de sensor se va a ocupar
        if(sensor==null) //checa si el dispositivo cuenta con el sensor
            finish();

        sensorEventListener=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float x=sensorEvent.values[0];
                System.out.println("valor giro"+x);
                if ((x<-5 && mov==0)||(x>5 && mov==1)){
                    mov++;
                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                    mostrarRand();
                }

                if (mov==2){
                    mov=0;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        //asociando atributos, id, layout
        imagen = (ImageView) findViewById(R.id.imageView);
        letrero = (TextView) findViewById(R.id.texto);
        btn = (Button) findViewById(R.id.button);
        layout = findViewById(R.id.layout);

        start();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarRand();
            }
        });
    }

    private void start(){
        sensorManager.registerListener(sensorEventListener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }
    private void stop(){
        sensorManager.unregisterListener(sensorEventListener);
    }

    public void mostrarRand() {
        //randoms();
        sr = ThreadLocalRandom.current().nextInt(0,6);

        lu = randArray[sr].getImagen();
        lux = randArray[sr].getAleatorio();

        if((lu!=templu)&&(lux!=templux)) {

            imagen.setImageResource(lu);
            letrero.setText(lux);

            //Random Color
            Random random = new Random();
            int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
            layout.setBackgroundColor(color);

            //Play Music
            stopPlying();
            music = MediaPlayer.create(this,randArray[sr].getSonido());
            music.start();

            //Vibrations
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            Random rand = new Random();
            int n = rand.nextInt(1000);
            long[] pattern = {n, n};
            vibrator.vibrate(pattern, 0);

            templu=lu;
            templux=lux;

        }else{
            mostrarRand();
        }

    }

    aleatorio i1 = new aleatorio(R.drawable.uno,    "Hedwig´s", R.raw.ring1);
    aleatorio i2 = new aleatorio(R.drawable.dos,    "Hunger Games", R.raw.ring2);
    aleatorio i3 = new aleatorio(R.drawable.tres,   "Dexys", R.raw.ring3);
    aleatorio i4 = new aleatorio(R.drawable.cuatro, "Bad Dream", R.raw.ring4);
    aleatorio i5 = new aleatorio(R.drawable.cinco,  "If I Saty", R.raw.ring4);
    aleatorio i6 = new aleatorio(R.drawable.seis,   "Me Before You", R.raw.ring5);
    aleatorio i7 = new aleatorio(R.drawable.siete,  "Chance The Rapper", R.raw.ring6);


    //llenando array
    aleatorio[] randArray = new aleatorio[]{ i1, i2, i3, i4, i5, i6, i7 };


    //metodo para permutar aleatoriamente una lista, utilizando como fuente el vector randArray
    public void randoms(){
        //  Collections.shuffle(Arrays.asList(randArray));
    }

    // METODO PARA REPRODUCIR
    private void stopPlying(){
        if(music != null){
            music.stop();
            music.release();
            music = null;
        }
    }
}

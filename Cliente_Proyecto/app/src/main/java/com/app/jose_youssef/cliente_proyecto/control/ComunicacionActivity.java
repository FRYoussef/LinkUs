package com.app.jose_youssef.cliente_proyecto.control;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.app.jose_youssef.cliente_proyecto.R;
import com.app.jose_youssef.cliente_proyecto.conexion.ManejadorConexion;
import com.app.jose_youssef.cliente_proyecto.control.difusion.DifusionFragment;
import com.app.jose_youssef.cliente_proyecto.control.asignatura.AsignaturasActivity;
import com.app.jose_youssef.cliente_proyecto.control.foro.ForoFragment;
import com.app.jose_youssef.cliente_proyecto.persistencia.ManejadorArchivoConfig;

import java.util.Observable;
import java.util.Observer;


public class ComunicacionActivity extends AppCompatActivity implements Observer{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String asigantura;
    private String usuario;
    private int tipoUsuario;
    private int tab = TAB_FORO;


    public final static int TAB_FORO = 0;
    public final static int TAB_DIFUSIONES = 1;
    public final static String KEY_TAB = "tab";
    private final String DIFUSIONES = "Difusiones";
    private final String FORO = "Foro";
    private final String NOTAS = "Notas";
    private final String ENCUESTAS = "Encuestas";

    private final String comunicaciones [] = new String []{DIFUSIONES, FORO, NOTAS, ENCUESTAS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunicacion);

        Intent intent = getIntent();
        asigantura = intent.getStringExtra(AsignaturasActivity.KEY_ASIGNATURA);
        usuario = intent.getStringExtra(LoginActivity.KEY_USUARIO);
        tipoUsuario = intent.getIntExtra(LoginActivity.KEY_TIPO_USUARIO, -1);
        tab = intent.getIntExtra(KEY_TAB, TAB_FORO);

        toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(asigantura);

        tabLayout = (TabLayout) findViewById(R.id.tl);
        viewPager = (ViewPager) findViewById(R.id.vpContenido);

        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(tab, true);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void update(Observable observable, Object o) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contextual, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logOut:
                ManejadorConexion.cierraRecursos();
                ManejadorArchivoConfig.eliminaDatos(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class SectionPagerAdapter extends FragmentPagerAdapter{

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putString(AsignaturasActivity.KEY_ASIGNATURA, asigantura);
            args.putString(LoginActivity.KEY_USUARIO, usuario);
            args.putInt(LoginActivity.KEY_TIPO_USUARIO, tipoUsuario);
            switch (position){
                case 0:
                    ForoFragment foro = new ForoFragment();
                    foro.setArguments(args);
                    return foro;
                case 1:
                    DifusionFragment difusion = new DifusionFragment();
                    difusion.setArguments(args);
                    return difusion;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return FORO;
                case 1:
                    return DIFUSIONES;
                default:
                    return null;
            }
        }
    }
}

package helpers.cristian.com.mapaunicorhelper;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import helpers.cristian.com.mapaunicorhelper.fragmentos.BloquesFragment;
import helpers.cristian.com.mapaunicorhelper.fragmentos.RutasFragment;

public class MainActivity extends AppCompatActivity {

    private PagerAdaptador pagerAdaptador;

    private ViewPager viewPager;
    private TabLayout tabs;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.pager);
        tabs = findViewById(R.id.tabs);
//        toolbar = findViewById(R.id.toolbar_main);
//        setSupportActionBar(toolbar);

        agregarFragmentosToTabs(viewPager, tabs);
    }

    // =============================================================================================
    // [ INICIO ] CONFIGUTACION DE LOS TABS CON LOS FRAGMENTS

    private void agregarFragmentosToTabs(ViewPager viewPager, TabLayout tabs){
        pagerAdaptador = new PagerAdaptador(getSupportFragmentManager());

        pagerAdaptador.agregarFragment(new BloquesFragment(), "BLOQUES");
        pagerAdaptador.agregarFragment(new RutasFragment(), "RUTAS");
        pagerAdaptador.agregarFragment(new BloquesFragment(), "MAPA");

        viewPager.setAdapter(pagerAdaptador);
        tabs.setupWithViewPager(viewPager);
    }

    public class PagerAdaptador extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentos;
        private ArrayList<String> titulos;

        public PagerAdaptador(FragmentManager fm) {
            super(fm);
            fragmentos = new ArrayList<>();
            titulos = new ArrayList<>();
        }

        public void agregarFragment(Fragment fragment, String titulo){
            this.fragmentos.add(fragment);
            this.titulos.add(titulo);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentos.get(position);
        }

        @Override
        public int getCount() {
            return fragmentos.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titulos.get(position);
        }
    }

    // [ FIN ] CONFIGUTACION DE LOS TABS CON LOS FRAGMENTS
    // =============================================================================================

}

package com.sunanda.monitorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.sunanda.monitorial.mModel.Student_info;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    private String roll = null;




    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });





        Student_info student_info = Student_info.getInstance();
        roll = student_info.getRoll();

















        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        //------------------------------------------------------------------

        //nav_share = create group;
        // nav_send = join group
        //assignment = send Assignment
        //getAssignment = find assignment

        Menu menu =navigationView.getMenu();
        MenuItem create_group = menu.findItem(R.id.nav_share);

        MenuItem join_group = menu.findItem(R.id.nav_send);
        MenuItem send_assignment = menu.findItem(R.id.assignment);
        MenuItem find_assignment = menu.findItem(R.id.getAssignment);




        if(roll != null)
        {
            create_group.setVisible(false);
            send_assignment.setVisible(false);
        }

        else
        {join_group.setVisible(false);
            find_assignment.setVisible(false);



        }























        LocationFragment locationFragment = new LocationFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .setCustomAnimations(R.anim.move_in,R.anim.move_out)
                .replace(
                        R.id.relativelayout_for_fragment,
                        locationFragment,
                        locationFragment.getTag()).commit();






    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera)

        {
            // Handle the camera action


            LocationFragment locationFragment = new LocationFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.move_in,R.anim.move_out)
                    .replace(
                            R.id.relativelayout_for_fragment,
                            locationFragment,
                            locationFragment.getTag()).commit();



        }

        else if (id == R.id.nav_gallery) {

            FindLocationFragment findLocationFragment = new FindLocationFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.move_in,R.anim.move_out)
                    .replace(
                            R.id.relativelayout_for_fragment,
                            findLocationFragment,
                            findLocationFragment.getTag()).commit();

        }












        else if (id == R.id.nav_share)
        {


//            startActivity(new Intent(NavigationActivity.this,Pop.class));



            CreateGroupFragment groupFragment = new CreateGroupFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.move_in,R.anim.move_out)
                    .replace(
                            R.id.relativelayout_for_fragment,
                            groupFragment,
                            groupFragment.getTag()).commit();




        }



else if (id == R.id.nav_send)
        {
            JoinGroupFragment joinGroupFragment = new JoinGroupFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.move_in,R.anim.move_out)
                    .replace(
                            R.id.relativelayout_for_fragment,
                            joinGroupFragment,
                            joinGroupFragment.getTag()).commit();


        }


        else if (id == R.id.assignment)
        {
           SendFileFragment sendFileFragment = new SendFileFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.move_in,R.anim.move_out)
                    .replace(
                            R.id.relativelayout_for_fragment,
                            sendFileFragment,
                            sendFileFragment.getTag()).commit();


        }


        else if (id == R.id.getAssignment)
        {
            FindAssignmentFragment findAssignmentFragment = new FindAssignmentFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.move_in,R.anim.move_out)
                    .replace(
                            R.id.relativelayout_for_fragment,
                            findAssignmentFragment,
                            findAssignmentFragment.getTag()).commit();


        }









        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

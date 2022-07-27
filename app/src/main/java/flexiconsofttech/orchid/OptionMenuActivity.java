package flexiconsofttech.orchid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class OptionMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected Context ctx = this;
    protected DataStorage storage;
    protected Toolbar toolbar;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.appmenu,menu);
        MenuItem cartid = menu.findItem(R.id.appcart);

        cartid.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ctx.startActivity(new Intent( ctx, Cart_Container.class));
                return true;

            }
        });

        return true;

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        storage = new DataStorage(ctx);
        int id = item.getItemId();

        switch (id)
        {
            case R.id.mehome :
                if(ctx instanceof Dashbord)
                {
                    if(drawerLayout.isDrawerOpen(GravityCompat.START))
                    {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                }
                else {

                    if(drawerLayout.isDrawerOpen(GravityCompat.START))
                    {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        ctx.startActivity(new Intent(this,Dashbord.class));
                        finish();
                    }

                }
                break;

            case R.id.mecart:
                if(drawerLayout.isDrawerOpen(GravityCompat.START))
                {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    ctx.startActivity(new Intent(this,Cart_Container.class));
                }
                break;

            case R.id.meshopbycategory :
                if (ctx instanceof Category_Container)
                {
                    if(drawerLayout.isDrawerOpen(GravityCompat.START))
                    {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                }
                else {

                    if(drawerLayout.isDrawerOpen(GravityCompat.START))
                    {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    ctx.startActivity(new Intent(this,Category_Container.class));
                    }

                 }
                break;


            case R.id.mewishlist:
                if(drawerLayout.isDrawerOpen(GravityCompat.START))
                {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    ctx.startActivity(new Intent(this,WishList_Container.class));
                }

                break;

            case R.id.mechangepassword :
                if(drawerLayout.isDrawerOpen(GravityCompat.START))
                {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    ctx.startActivity(new Intent(this,Change_Password.class));
                }
                break;

            case  R.id.melogout :
                storage.write("id",0);
                ctx.startActivity(new Intent(ctx,MainActivity.class));
                finish();
                break;
        }

        return true;

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

}

package uwi.dcit.AgriExpenseTT;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.widget.Toast;

import uwi.dcit.AgriExpenseTT.helpers.NavigationControl;
import uwi.dcit.AgriExpenseTT.helpers.NetworkHelper;
import uwi.dcit.AgriExpenseTT.helpers.SignInManager;


public abstract class BaseActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks , NavigationControl {

    protected SignInManager signInManager;
    protected Fragment leftFrag,rightFrag;
    protected NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR); // Request Feature must be called before adding content
        super.onCreate(savedInstanceState);
        signInManager = new SignInManager(BaseActivity.this,BaseActivity.this);
    }

    public void setupNavDrawer(){
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        if (mNavigationDrawerFragment != null)
            mNavigationDrawerFragment.setUp( R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position){
            case 0:
                // Home
                startActivity(new Intent(getApplicationContext(), Main.class));
                break;
            case 1:
                //new cycle
                startActivity(new Intent(getApplicationContext(), NewCycle.class));
                break;
            case 2:
                //new purchase
                startActivity(new Intent(getApplicationContext(), NewPurchase.class));
                break;
            case 3:
                //hire labour
                startActivity(new Intent(getApplicationContext(), HireLabour.class));
                break;
            case 4:
                //report manager
                startActivity(new Intent(getApplicationContext(),ManageReport.class));
                break;
            case 5:
                // manage data
                startActivity(new Intent(getApplicationContext(),ManageData.class));
                break;
            case 6:
                backUpData();
                break;

        }
    }

    public void backUpData(){
        Intent i = new Intent(getApplicationContext(), Backup.class);
        if (this.signInManager.isExisting() == null){ 			// User does not exist => check Internet and then create user
            if (!NetworkHelper.isNetworkAvailable(this)){ 		// No network available so display appropriate message
                Toast.makeText(getApplicationContext(), "No internet connection, Unable to sign-in at the moment.", Toast.LENGTH_LONG).show();
                return;
            }
            i.putExtra("ACTION",  Backup.SIGN_UP); 				// Launch the Backup activity with the sign-up action passed
        }else if (!this.signInManager.isSignedIn()){ 			// If not signed attempt to login with existing account
            i.putExtra("ACTION",  Backup.SIGN_IN); 				// Launch the Backup activity with the sign-in action passed
        }else i.putExtra("ACTION", Backup.VIEW);				// Launch the Backup activity to simply view the data because user is existing and signed in
        startActivity(i);
    }

    @Override
    public void navigate(Fragment oldFrag,Fragment newFrag) {

    }

    @Override
    public Fragment getLeftFrag() {
        return leftFrag;
    }
    @Override
    public Fragment getRightFrag() {
        return rightFrag;
    }

    @Override
    public String[] getMenuOptions() {
        return new String[]{
                getString(R.string.menu_item_home),
                getString(R.string.menu_item_newCycle),
                getString(R.string.menu_item_newPurchase),
                getString(R.string.menu_item_hireLabour),
                getString(R.string.menu_item_genFile),
                getString(R.string.menu_item_manageData),
                getString(R.string.menu_item_signIn)
        };
    }

    @Override
    public int[] getMenuImages() {
        return new int[]{
                R.drawable.ic_home_black_36dp,
                R.drawable.mainmenu_cycle_triangle,
                R.drawable.mainmenu_shopping_cart,
                R.drawable.mainmenu_shovel_single,
                R.drawable.mainmenu_reports,
                R.drawable.mainmenu_data_settings,
                R.drawable.mainmenu_signin
        };
    }
}

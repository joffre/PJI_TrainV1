package fr.pji.train;

import java.util.ArrayList;

import fr.pji.train.fragments.AProposFragment;
import fr.pji.train.fragments.AccueilFragment;
import fr.pji.train.fragments.NotesFragment;
import fr.pji.train.fragments.POIsFragment;
import fr.pji.train.fragments.PreferencesFragment;
import fr.pji.train.navigationdrawer.NavDrawerItem;
import fr.pji.train.navigationdrawer.NavDrawerListAdapter;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;

public class Accueil extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	private NavigationDrawerFragment mNavigationDrawerFragment;
	private DrawerLayout mDrawerLayout;
	private CharSequence mDrawerTitle;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	private String[] navMenuTitles;
	private String[] navMenuIcons;
	
	public static String preferenceVue;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accueil);

		final FrameLayout frame = (FrameLayout) findViewById(R.id.container);
		
		this.preferenceVue = "Carte";

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		navMenuTitles = getResources().getStringArray(
				R.array.navigationDrawer_items);
		navMenuIcons = getResources().getStringArray(
				R.array.navigationDrawer_icons);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons[0],
				false, "22"));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons[1],
				false, "22"));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons[2],
				false, "22"));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons[3],
				false, "22"));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons[4],
				false, "22"));

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.navigation_drawer);

		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// On affiche la barre de menu de gauche
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		// Configuration de la barre de menu de gauche
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

	}

	public void onCheckboxClicked(View view) {
	    CheckBox checkBoxCarte = (CheckBox) findViewById(R.id.checkbox_carte);
	    CheckBox checkBoxMetro = (CheckBox) findViewById(R.id.checkbox_metro);
	    
	    if (checkBoxCarte.isChecked()) {
			this.preferenceVue = "Carte";
		} else if (checkBoxMetro.isChecked()) {
			this.preferenceVue = "Métro";
		} else {
			this.preferenceVue = "Carte";
		}
	}

	private class SlideMenuClickListener implements
			AdapterView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// Affiche le fragment correspondant à l'élèment sélectionné dans la
			// barre de menu de gauche
			onNavigationDrawerItemSelected(position);
			// Permet de fermer la barre de menu de gauche
			mDrawerLayout.closeDrawer(mDrawerList);
		}
	}

	@Override
	// Affiche le fragment correspondant à l'élèment sélectionné dans la barre
	// de menu de gauche
	public void onNavigationDrawerItemSelected(int position) {
		FragmentManager fragmentManager = getFragmentManager();
		if (position == 0) {
			fragmentManager.beginTransaction()
			.replace(R.id.container, POIsFragment.newInstance())
			.commit();
		} else if (position == 1) {
			fragmentManager.beginTransaction()
					.replace(R.id.container, NotesFragment.newInstance())
					.commit();
		} else if (position == 2) {
			fragmentManager.beginTransaction()
			.replace(R.id.container, AccueilFragment.newInstance())
			.commit();
		} else if (position == 3) {
			fragmentManager.beginTransaction()
					.replace(R.id.container, PreferencesFragment.newInstance())
					.commit();
		} else if (position == 4) {
			fragmentManager.beginTransaction()
					.replace(R.id.container, AProposFragment.newInstance())
					.commit();
		}
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		case 4:
			mTitle = getString(R.string.title_section4);
			break;
		case 5:
			mTitle = getString(R.string.title_section5);
			break;
		default:
			mTitle = getString(R.string.app_name);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.accueil, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_pois,
					container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((Accueil) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

}

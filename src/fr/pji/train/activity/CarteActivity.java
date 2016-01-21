package fr.pji.train.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;

import fr.pji.train.R;
import fr.pji.train.bean.Noeud;
import fr.pji.train.bean.Poi;
import fr.pji.train.dialog.PoiDialog;
import fr.pji.train.liste.ListeNoeuds;
import fr.pji.train.liste.ListePois;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CarteActivity extends Activity implements LocationListener {

	private MapView mMapView;
	private IMapController mMapController;
	private OverlayItem current_position;
	private LocationManager lManager;
	private PathOverlay myPath;
	private ItemizedIconOverlay<OverlayItem> affichageGares;
	private ItemizedIconOverlay<OverlayItem> affichagePois;
	private List<Noeud> gares;
	private ListePois listePois;
	private TextView distanceTerminusTextView;
	private TextView kmCourantTextView;
	private TextView poiProcheTextView;
	private Location debutLigne;
	private Location finLigne;
	private Location dernierLocation;
	private List<TextView> listeVuePoi;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carte);
		
		distanceTerminusTextView = (TextView)findViewById(R.id.distance);
		distanceTerminusTextView.setText("Localisation en cours...");
		
		listeVuePoi = new ArrayList<TextView>();
		TextView poi1 = (TextView)findViewById(R.id.poi1);
		TextView poi2 = (TextView)findViewById(R.id.poi2);
		TextView poi3 = (TextView)findViewById(R.id.poi3);
		TextView poi4 = (TextView)findViewById(R.id.poi4);
		TextView poi5 = (TextView)findViewById(R.id.poi5);

		listeVuePoi.add(poi1);
		listeVuePoi.add(poi2);
		listeVuePoi.add(poi3);
		listeVuePoi.add(poi4);
		listeVuePoi.add(poi5);
		
		kmCourantTextView = (TextView)findViewById(R.id.kmcourant);
		poiProcheTextView = (TextView)findViewById(R.id.poiproche);

		Bundle b    = getIntent().getExtras();
	    ListeNoeuds listeNodes    = b.getParcelable("listeNoeud");

	    listePois = b.getParcelable("listePois");

	    gares = new ArrayList<Noeud>();
		
		// Création de la carte
		this.mMapView = (MapView) findViewById(R.id.mapView);
		this.mMapView.setBuiltInZoomControls(true);
		this.mMapView.setMultiTouchControls(true);

		this.mMapController = this.mMapView.getController();
		// Configuration du zoom
		this.mMapController.setZoom(15);
		
		// Ligne de Train
		myPath = new PathOverlay(Color.RED, this);
		
		for(Noeud noeud : listeNodes){
			if(noeud.isEstUneGare()){
				gares.add(noeud);
			}else{
				GeoPoint pt = new GeoPoint(Double.valueOf(noeud.getLat()),Double.valueOf(noeud.getLon()));
	
				myPath.addPoint(pt);
			}
		}
		listeNodes.removeAll(gares);
		
		finLigne = new Location("finLigne");
		finLigne.setLatitude(Double.valueOf(listeNodes.get(listeNodes.size()-1).getLat()));
		finLigne.setLongitude(Double.valueOf(listeNodes.get(listeNodes.size()-1).getLon()));

		debutLigne = new Location("debutLigne");
		debutLigne.setLatitude(Double.valueOf(listeNodes.get(0).getLat()));
		debutLigne.setLongitude(Double.valueOf(listeNodes.get(0).getLon()));
		
		
		myPath.getPaint().setStrokeWidth(3.0f);
		
		//Affichage des gares
		
		ArrayList<OverlayItem> mItems = new ArrayList<OverlayItem>();
		
		for(Noeud noeud : gares){
			OverlayItem gare = new OverlayItem("gare", noeud.getNom(), new GeoPoint(Double.valueOf(noeud.getLat()),Double.valueOf(noeud.getLon())));
			Drawable newMarker = this.getResources().getDrawable(R.drawable.gare);
			gare.setMarker(newMarker);
			mItems.add(gare);
		}
		affichageGares = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), mItems, new OnItemGestureListener<OverlayItem>() {
        @Override public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
        	Toast.makeText(CarteActivity.this, item.getTitle()+": "+item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
        }
        @Override public boolean onItemLongPress(final int index, final OverlayItem item) {
                return true;
        }
      } );
		
		
		//affichage des pois
		ArrayList<OverlayItem> pItems = new ArrayList<OverlayItem>();

		for(Poi poi : listePois){
			
			Location locationT = new Location("poi");
			locationT.setLatitude(Double.valueOf(poi.getLat()));
			locationT.setLongitude(Double.valueOf(poi.getLon()));
			float distance = debutLigne.distanceTo(locationT);
			poi.setPointKilometrique(Math.round(distance/1000));
			
			OverlayItem poInt = new OverlayItem("Point d'intêret", poi.getNom(), new GeoPoint(Double.valueOf(poi.getLat()),Double.valueOf(poi.getLon())));
			Drawable newMarker = this.getResources().getDrawable(R.drawable.poi);
			poInt.setMarker(newMarker);
			pItems.add(poInt);
		}
		affichagePois = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), pItems, new OnItemGestureListener<OverlayItem>() {
	        @Override public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
	        	Toast.makeText(CarteActivity.this, item.getTitle()+" "+item.getSnippet(), Toast.LENGTH_SHORT).show();
	                return true;
	        }
	        @Override public boolean onItemLongPress(final int index, final OverlayItem item) {
	                return true;
	        }
	      } );
		
		
		
		// Ajout de la ligne de train
		this.mMapView.getOverlays().add(myPath);

		// Ajout des gares
		this.mMapView.getOverlays().add(affichageGares);
		
		// Ajout des pois
		this.mMapView.getOverlays().add(affichagePois);
		

		// Centrage de la carte sur le premier point de la ligne
		this.mMapController.setCenter(new GeoPoint(Double.valueOf(listeNodes.get(0).getLat()),Double.valueOf(listeNodes.get(0).getLon())));

		this.mMapView.invalidate();
		
	    lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 100, this);
	    
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.carte, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onLocationChanged(Location location) {		
		ArrayList<OverlayItem> mItems = new ArrayList<OverlayItem>();
		dernierLocation = location;
		float kilometreCourant = (debutLigne.distanceTo(location)/1000)+1.0f;
		DecimalFormat df = new DecimalFormat("0.00");
		distanceTerminusTextView.setText("Terminus: "+df.format(location.distanceTo(finLigne)/1000)+"km");
		kmCourantTextView.setText("Km courant: "+df.format(kilometreCourant));
		
		ListePois poiSupp = new ListePois(); 
		boolean procheOuDans = false;
		for(Poi poi : listePois){			
			if(poi.getDebutVisible() < kilometreCourant && poi.getFinVisible() > kilometreCourant){
				poiProcheTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.alerte_dans_zone));
				poi.setDistance(0f);
				procheOuDans = true;
			}else if(poi.getDebutVisible() > kilometreCourant){
				poi.setDistance(((float)poi.getDebutVisible()) - kilometreCourant);
				if((poi.getDebutVisible() - kilometreCourant) < 0.5f){
					poiProcheTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.alerte_proche));
					procheOuDans = true;
				}
			}else{
				//poi.setDistance(((float)poi.getFinVisible()) - kilometreCourant);
				poiSupp.add(poi);
			}
		}
		listePois.removeAll(poiSupp);
		if(!procheOuDans){
			poiProcheTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.alerte_loin));
		}
		Collections.sort(listePois);
		Poi prochainPoi = listePois.getProchainPoi();
		poiProcheTextView.setText(prochainPoi != null ? "Prochain POI:\n"+prochainPoi.getNom() : "Tous les POIs ont été dépassés");

		for(int i=0;i<listeVuePoi.size();i++){
			TextView textview = listeVuePoi.get(i);
			if(listePois.size() > i){
				Poi poi = listePois.get(i);
				textview.setText(df.format(poi.getDistance())+"km :"+poi.getNom());
				setOnClickListenerPoiTextView(textview, i);
			}else{
				setOnClickListenerPoiTextView(textview, -1);
			}
		}
		
		this.mMapView.getOverlays().clear();
		this.mMapView.getOverlays().add(myPath);
		this.mMapView.getOverlays().add(affichageGares);
		current_position = new OverlayItem("Here", "votre position", new GeoPoint(location.getLatitude(),location.getLongitude()));
		Drawable newMarker = this.getResources().getDrawable(R.drawable.position);
		current_position.setMarker(newMarker);
		mItems.add(current_position);
		
		ItemizedIconOverlay<OverlayItem> myLocation = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), mItems, new OnItemGestureListener<OverlayItem>() {
        @Override public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
        	Toast.makeText(CarteActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();

                return true;
        }
        @Override public boolean onItemLongPress(final int index, final OverlayItem item) {
                return true;
        }
      } );
		
		// Ajout de la localisation
		this.mMapView.getOverlays().add(myLocation);
		this.mMapView.getOverlays().add(affichagePois);
		this.mMapView.invalidate();
		
	}
	
	public void setOnClickListenerPoiTextView(TextView view, final int position){
		if(position == -1){
			view.setOnClickListener(null);
			view.setText("");
			return;
		}
		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				PoiDialog poiDialog = new PoiDialog(listePois.get(position), CarteActivity.this);
				poiDialog.afficher();
				
			}
		});
	}
	
	public void centrerEcran(View vue){
		if(dernierLocation != null){
			this.mMapController.setCenter(new GeoPoint(dernierLocation));
		}
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
}

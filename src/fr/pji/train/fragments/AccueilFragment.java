package fr.pji.train.fragments;

import java.util.List;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.PathOverlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.SimpleLocationOverlay;

import fr.pji.train.Accueil;
import fr.pji.train.surfaceview.LigneMetroSurfaceView;
import android.app.ActionBar.LayoutParams;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class AccueilFragment extends Fragment {

	private MapView mMapView;
	private ResourceProxy mResourceProxy;
	private IMapController mMapController;

	protected List<String> points;

	private ScaleBarOverlay mScaleBarOverlay;
	private SimpleLocationOverlay mMyLocationOverlay;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View nullView = null;

		if (Accueil.preferenceVue == "Carte") {
			// Création de la carte
			this.mMapView = new MapView(getActivity(), 256);

			this.mMapView.setBuiltInZoomControls(true);
			this.mMapView.setMultiTouchControls(true);

			this.mMapController = this.mMapView.getController();

			this.mMapController = this.mMapView.getController();

			// Dunkerque
			GeoPoint gPt0 = new GeoPoint(51.03036, 2.3684999);
			// Coudekerque-Branche
			GeoPoint gPt1 = new GeoPoint(51.0177621, 2.3751331);
			// Bergues
			GeoPoint gPt2 = new GeoPoint(50.9688922, 2.4255408);
			// Escquelbecq
			GeoPoint gPt3 = new GeoPoint(50.889785, 2.4121583);
			// Arnèke
			GeoPoint gPt4 = new GeoPoint(50.8315184, 2.4083297);
			// Cassel
			GeoPoint gPt5 = new GeoPoint(50.7876597, 2.4598073);
			// Hazebrouck
			GeoPoint gPt6 = new GeoPoint(50.7250487, 2.5423207);
			// Bailleul
			GeoPoint gPt7 = new GeoPoint(50.7289396, 2.734806);
			// Nieppe
			GeoPoint gPt8 = new GeoPoint(50.6967112, 2.8289803);
			// Armentières
			GeoPoint gPt9 = new GeoPoint(50.6805854, 2.8775278);
			// Pérenchies
			GeoPoint gPt10 = new GeoPoint(50.6672331, 2.9770987);
			// Lille Flandres
			GeoPoint gPt11 = new GeoPoint(50.6359029, 3.0706419);

			// Ligne de Train
			PathOverlay myPath = new PathOverlay(Color.RED, getActivity());

			myPath.addPoint(gPt0);
			myPath.addPoint(gPt1);
			myPath.addPoint(gPt2);
			myPath.addPoint(gPt3);
			myPath.addPoint(gPt4);
			myPath.addPoint(gPt5);
			myPath.addPoint(gPt6);
			myPath.addPoint(gPt7);
			myPath.addPoint(gPt8);
			myPath.addPoint(gPt9);
			myPath.addPoint(gPt10);
			myPath.addPoint(gPt11);
			myPath.getPaint().setStrokeWidth(10.0f);

			// Ajout de la ligne de train
			this.mMapView.getOverlays().add(myPath);
			// Configuration du zoom
			this.mMapController.setZoom(14);
			// Centrage de la carte sur le premier point de la ligne
			this.mMapController.setCenter(gPt0);

			this.mMapView.invalidate();

			return this.mMapView;
		} else if (Accueil.preferenceVue == "Métro") {
			LigneMetroSurfaceView ligneMetro = new LigneMetroSurfaceView(
					getActivity());

			return ligneMetro;
		}

		return nullView;
	}

	public static AccueilFragment newInstance() {
		return new AccueilFragment();
	}

}

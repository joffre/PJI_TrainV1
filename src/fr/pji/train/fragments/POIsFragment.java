package fr.pji.train.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import fr.pji.train.R;
import fr.pji.train.activity.CarteActivity;
import fr.pji.train.bean.Noeud;
import fr.pji.train.liste.ListeNoeuds;
import fr.pji.train.liste.ListePois;
import fr.pji.train.location.listener.RelationLocationListeneur;
import fr.pji.train.location.listener.RelationsOnItemSelectedListeneur;
import fr.pji.train.utilitaires.ParseurXmlToBean;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import fr.pji.train.bean.Relation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class POIsFragment extends Fragment {

	private View view;

	private LocationManager lManager;

	
	private ListeNoeuds listeNodes;
	private ListePois listePois;
	private String idLigne;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_pois, null);

		lManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);

		if (lManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			new RelationLocationListeneur(getResources().getString(
					R.string.URL_GET_RELATION_NAME), new XmlTask(), lManager, this);
		} else {
			Toast.makeText(
					getActivity(),
					"Vous devez activer le GPS pour pouvoir utiliser l'application",
					Toast.LENGTH_LONG).show();
			Intent myIntent = new Intent(
					Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(myIntent);
		}
		
		return view;
	}

	public void populateListeRelations(String result){
		
		Spinner spinner = (Spinner) view
				.findViewById(R.id.spinner);

		List<String> listeTER = new ArrayList<String>();
		
		List<Relation> listeRelations = ParseurXmlToBean
				.parseXmlToRelationList(result);
		listeTER.add("Veuillez sélectionnez une ligne");
		for(Relation rel : listeRelations){
			listeTER.add(rel.getNom()+": "+rel.getIdentifiant());
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				listeTER);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		OnItemSelectedListener listener = new RelationsOnItemSelectedListeneur(getResources().getString(R.string.URL_GET_NODES), new XmlTask(), this);
		spinner.setOnItemSelectedListener(listener);
	
	}
	
	public void populateListeNoeud(String result, String[] nomLigne){
		listeNodes = ParseurXmlToBean.parseXmlToNoeudList(result);
		idLigne = nomLigne[1].trim();
		
		XmlTask asynTache = new XmlTask();
		asynTache.execute(String.format(getResources().getString(R.string.URL_GET_POIS), idLigne));
			String xml;
			try {
				xml = asynTache.get();
			 
			 	// CODE INUTILE SI LE SERVEUR EST DEPLOYE
				if (xml.equals("")) {
					AssetManager asm = this.getResources().getAssets();

						StringBuilder response = new StringBuilder();
						InputStream is = asm.open("getPoisFromRelation.xml");
						
						BufferedReader br = new BufferedReader(
								new InputStreamReader(is));
						String line;
						while ((line = br.readLine()) != null) {
							response.append(line);
						}
						
						xml = response.toString();
					
				}
				
				listePois = ParseurXmlToBean.parseXmlToPoiList(xml);
				
				Intent intent = new Intent(this.getActivity(),CarteActivity.class);
				intent.putExtra("listeNoeud", (Parcelable)listeNodes);
				intent.putExtra("listePois", (Parcelable)listePois);
				
				startActivity(intent);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static POIsFragment newInstance() {
		return new POIsFragment();
	}

	public class XmlTask extends AsyncTask<String, Void, String> {
		

		@Override
		protected void onPreExecute() {
			Toast.makeText(getActivity(),
					"Chargement des données en cours, veuillez patientez...",
					Toast.LENGTH_LONG).show();
		}

		@Override
		protected String doInBackground(String... arg0) {

			StringBuilder response = new StringBuilder();
			try {
				HttpGet get = new HttpGet();
				get.setURI(new URI(arg0[0]));
				
				/*Ce timeout est trop court si le serveur est déployé, il est positionné à une seconde pour que les fichiers XML soient utilisés*/
				int timeout = 1000;
				HttpParams httpParameters = new BasicHttpParams();
				// Set the timeout in milliseconds until a connection is established.
				// The default value is zero, that means the timeout is not used. 
				int timeoutConnection = timeout;
				HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
				// Set the default socket timeout (SO_TIMEOUT) 
				// in milliseconds which is the timeout for waiting for data.
				int timeoutSocket = timeout;
				HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

				DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
				HttpResponse httpResponse = httpClient.execute(get);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					Log.d("[GET REQUEST]", "HTTP Get succeeded");

					HttpEntity messageEntity = httpResponse.getEntity();
					InputStream is = messageEntity.getContent();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(is));
					String line;
					while ((line = br.readLine()) != null) {
						response.append(line);
					}
				}
			} catch (Exception e) {
				Log.e("[GET REQUEST]", e.getMessage() !=null ? e.getMessage() : "erreur");
			}
			Log.d("[GET REQUEST]", "Done with HTTP getting");

			return response.toString();
		}

	}

}

package fr.pji.train.liste;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import fr.pji.train.bean.Poi;

public class ListePois extends ArrayList<Poi> implements Parcelable {
	
	 public ListePois()
	    {
	 
	    }
	 
	    public ListePois(Parcel in)
	    {
	        this.getFromParcel(in);
	    }
	 
	    @SuppressWarnings("rawtypes")
	    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
	    {
	        public ListePois createFromParcel(Parcel in)
	        {
	            return new ListePois(in);
	        }
	 
	        @Override
	        public Object[] newArray(int size) {
	            return null;
	        }
	    };
	 
	    @Override
	    public int describeContents() {
	        return 0;
	    }
	 
	    @Override
	    public void writeToParcel(Parcel dest, int flags)
	    {
	        //Taille de la liste
	        int size = this.size();
	        dest.writeInt(size);
	        for(int i=0; i < size; i++)
	        {
	            Poi poi = this.get(i); //On vient lire chaque objet personne
	    		dest.writeString(poi.getNom());
	    		dest.writeString(poi.getLat());
	    		dest.writeString(poi.getLon());
	    		dest.writeString(poi.getId());
	    		dest.writeString(poi.getCommentaire());
	    		dest.writeString(poi.getCategorie());
	    		dest.writeFloat(poi.getDebutVisible());
	    		dest.writeFloat(poi.getFinVisible());
	    		dest.writeString(poi.getLienImage());
	    		dest.writeString(poi.getLienWeb());
	        }
	    }
	 
	    public void getFromParcel(Parcel in)
	    {
	        // On vide la liste avant tout remplissage
	        this.clear();
	 
	        //Récupération du nombre d'objet
	        int size = in.readInt();
	 
	        //On repeuple la liste avec de nouveau objet
	        for(int i = 0; i < size; i++)
	        {
	            Poi poi = new Poi();
	            poi.setNom(in.readString());
	            poi.setLat(in.readString());
	            poi.setLon(in.readString());
	            poi.setId(in.readString());
	            poi.setCommentaire(in.readString());
	            poi.setCategorie(in.readString());
	            poi.setDebutVisible(in.readFloat());
	    		poi.setFinVisible(in.readFloat());
	    		poi.setLienImage(in.readString());
	    		poi.setLienWeb(in.readString());
	            this.add(poi);
	        }
	 
	    }
	    
	    /**
	     *  Permet de recuperer le poi avec la plus faible distance superieur a zero, la liste doit avoir ete trie par distance avant
	     *  
	     *  @return le poi avec la plus faible distance superieur a zero ou null si tout les POIs ont ete vu
	     * */
	    public Poi getProchainPoi(){
	    	for(int i=0;i<this.size();i++){
	    		if(this.get(i).getDistance() > 0){
	    			return this.get(i);
	    		}
	    	}
	    	return null;
	    }

}

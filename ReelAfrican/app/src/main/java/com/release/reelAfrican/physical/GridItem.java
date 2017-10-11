package com.release.reelAfrican.physical;


public class GridItem {
	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}

	private String title;
	private String videoType;
	private String productid;
	public String getCurrencyid() {
		return currencyid;
	}

	public void setCurrencyid(String currencyid) {
		this.currencyid = currencyid;
	}

	private String imageId;
	private String currencyid;
	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getDescrip() {
		return descrip;
	}

	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}



	public String getStockstatus() {
		return stockstatus;
	}

	public void setStockstatus(String stockstatus) {
		this.stockstatus = stockstatus;
	}

	private String permalink;
	private int isConverted;
	private int isPPV;
	private int isAPV;
	private String price;
	private String descrip;
	private String stockstatus;

	public int getIsAPV() {
		return isAPV;
	}

	public void setIsAPV(int isAPV) {
		this.isAPV = isAPV;
	}

	public int getIsPPV() {
		return isPPV;
	}

	public void setIsPPV(int isPPV) {
		this.isPPV = isPPV;
	}

	public int getIsConverted() {
		return isConverted;
	}

	public void setIsConverted(int isConverted) {
		this.isConverted = isConverted;
	}


	public String getMovieUniqueId() {
		return movieUniqueId;
	}

	public void setMovieUniqueId(String movieUniqueId) {
		this.movieUniqueId = movieUniqueId;
	}

	public String getMovieStreamUniqueId() {
		return movieStreamUniqueId;
	}

	public void setMovieStreamUniqueId(String movieStreamUniqueId) {
		this.movieStreamUniqueId = movieStreamUniqueId;
	}

	private String movieUniqueId;
	private String movieStreamUniqueId;

	public String getIsEpisode() {
		return isEpisode;
	}

	public void setIsEpisode(String isEpisode) {
		this.isEpisode = isEpisode;
	}

	private String isEpisode;

	public String getPermalink() {
		return permalink;
	}

	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	private String videoUrl;

	public String getVideoTypeId() {
		return videoTypeId;
	}

	public void setVideoTypeId(String videoTypeId) {
		this.videoTypeId = videoTypeId;
	}

	public String getMovieGenre() {
		return movieGenre;
	}

	public void setMovieGenre(String movieGenre) {
		this.movieGenre = movieGenre;
	}

	private String videoTypeId;
	private String movieGenre;
	private String currencysymbol;
	public String getVideoType() {

		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}


	public String getCurrencysymbol() {
		return currencysymbol;
	}

	public void setCurrencysymbol(String currencysymbol) {
		this.currencysymbol = currencysymbol;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	//	public GridItem(int imageId, String title,String rating) {
//		super();
//		this.imageId = imageId;
//		this.title = title;
//		this.rating = rating;
//	}
public GridItem(String imageId, String title, String videoType, String videoTypeId, String movieGenre, String videoUrl, String permalink, String isEpisode, String movieUniqueId, String movieStreamUniqueId, int isConverted, int isPPV, int isAPV, String price, String descrip,String currencyid,String productid,String stockstatus,String currencysymbol) {
	super();
	this.imageId = imageId;
	this.title = title;
	this.videoType = videoType;
	this.videoTypeId = videoTypeId;
	this.movieGenre = movieGenre;
	this.videoUrl = videoUrl;
	this.permalink = permalink;
	this.isEpisode = isEpisode;
	this.movieUniqueId = movieUniqueId;
	this.movieStreamUniqueId = movieStreamUniqueId;
	this.isConverted = isConverted;
	this.isPPV = isPPV;

	this.isAPV = isAPV;
	this.price = price;

	this.descrip = descrip;
	this.currencyid = currencyid;
	this.productid = productid;
	this.stockstatus = stockstatus;
	this.currencysymbol = currencysymbol;

}
	public String getImage() {
		return imageId;
	}

	public void setImage(String imageId) {

		this.imageId = imageId;
	}
	/*public String getImage() {
		return imageId;
	}

	public void setImage(String imageId) {
		this.imageId = imageId;
	}*/

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}

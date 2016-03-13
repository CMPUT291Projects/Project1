public class AutoSale
{
	public int transaction_id;
	public String seller_id;
	public String buyer_id;
	public String vehicle_id;
	public Date s_date;
	public float price;

	public AutoSale(int transaction_id, String seller_id, String buyer_id, 
			String vehicle_id, Date s_date, float price)
	{
		this.transaction_id = transaction_id;
		this.seller_id = seller_id;
		this.buyer_id = buyer_id;
		this.vehicle_id = vehicle_id;
		this.s_date = s_date;
		this.price = price;
	} 
}

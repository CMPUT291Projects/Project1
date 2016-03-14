import java.util.Date;

public class auto_sale
{
	public Integer transaction_id;
	public String seller_id;
	public String buyer_id;
	public String vehicle_id;
	public Date s_date;
	public Float price;

	public auto_sale(Integer transaction_id, String seller_id, String buyer_id,
			String vehicle_id, Date s_date, Float price)
	{
		this.transaction_id = transaction_id;
		this.seller_id = seller_id;
		this.buyer_id = buyer_id;
		this.vehicle_id = vehicle_id;
		this.s_date = s_date;
		this.price = price;
	}
}

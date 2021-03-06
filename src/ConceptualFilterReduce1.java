import java.io.IOException;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class ConceptualFilterReduce1 extends Reducer<Text, Text, Text, Text>
{	
	public void reduce(Text docnametext, Iterable<Text> records, Context context) throws IOException, InterruptedException
	{
		
		String fingerprint = "", recordstring;
		int startdollar, row;
		float value;
		boolean firsttime = true;
		TreeMap<Integer, Float> fingermap = new TreeMap<Integer, Float>();
		
		for(Text record: records)
		{
			recordstring = record.toString();
			startdollar = recordstring.indexOf("$$$");
			row = Integer.parseInt(recordstring.substring(0, startdollar).trim());
			value = Float.parseFloat(recordstring.substring(startdollar+3).trim());
			fingermap.put(row, value);
		}
		
		for(Entry<Integer, Float> i: fingermap.entrySet())
		{
				if(firsttime)
				{
					fingerprint += i.getValue();
					firsttime = false;
				}
				else
					fingerprint += ","+i.getValue();
		}
		
		//System.out.println("I(N reducer\n");
		context.write(docnametext, new Text(" $$$ " + fingerprint));
		//context.write(new Text("temp"), new Text("dollarS"));
		
	}
}

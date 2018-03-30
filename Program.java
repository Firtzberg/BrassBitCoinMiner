
import java.security.MessageDigest;

public class Program{

	public static class Block{
		public int index;
		public String minedBy;
		public String data;
		public String previousHash;
		public int nonce;
		public String hash;

		public String getContent(){
			return String.valueOf(index) + " " +
				minedBy + " " +
				data + " " +
				previousHash + " " +
				String.valueOf(nonce);

		}

		public boolean updateHash(){
			MessageDigest digest;
			try{
				digest = MessageDigest.getInstance("SHA-256");
			}
			catch (Exception e){
				System.out.println(e.toString());
				return false;
			}
			digest.update(this.getContent().getBytes());
			byte[] digested = digest.digest();
			StringBuilder sb = new StringBuilder();
			int position = 0;
			for (;position < digested.length; position++)
			{
				int value = digested[position];
				if (value < 0)
					value += 256;
				char character = '0';
				sb.append(getChar(value/16));
				sb.append(getChar(value%16));
			}
			String hash = sb.toString();
			int index = 0;
			for (index = 0; index < 4; index ++){
				if (hash.charAt(index) != '0'){
					return false;
				}
			}
			this.hash = hash;
			return true;
		}

		public void setData(Transaction[] transactions){
			StringBuilder sb = new StringBuilder();
			if (transactions.length > 0){
				sb.append(transactions[0]);
			}
			int transactionIndex = 1;
			for(;transactionIndex<transactions.length;transactionIndex++){
				sb.append('\n');
				sb.append(transactions[transactionIndex].toString());
			}
			data = sb.toString();
		}

		public static char getChar(int hex){
			if (hex < 10)
				return (char)(hex+'0');
			return (char)(hex-10+'A');
		}

		public void findHash(){
			while(!updateHash()){
				nonce++;
			}
		}

		public String toString(){
			return getContent() + " " + this.hash;
		}
	}

	public static class Transaction{
		String from;
		String to;
		int amount;
		public String toString(){
			return "Transaction," + from + "," + to + "," + String.valueOf(amount);
		}
	}

	public static Block getGenesisBlock(){
		Block genesisBlock = new Block();
		genesisBlock.index = 0;
		genesisBlock.minedBy = "Genesis";
		genesisBlock.data = "Genesis";
		genesisBlock.previousHash = "0";
		genesisBlock.nonce = 52458;
		genesisBlock.updateHash();
		return genesisBlock;
	}

	public static void main(String[] args){
		Block nextBlock = new Block();
		nextBlock.index = 55;
		nextBlock.minedBy = "Hrvoje";
		Transaction transaction = new Transaction();
		transaction.from = "David";
		transaction.to = "Hrvoje";
		transaction.amount = 50;
		nextBlock.setData(new Transaction[]{transaction});
		nextBlock.previousHash = "000028DD5C4DAB3E13C979EE5A21258671CBC64C9DF19E2F20DBF15C4DBFAF97";
		nextBlock.findHash();
		System.out.println(nextBlock);
	}
}
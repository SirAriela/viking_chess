public abstract class ConcretePiece  implements Piece  {
   // private Player owner;
    //private String typeOfPiece;
   protected Player owner;
    protected int eatenPieces = 0;

        //private String typeOfPiece;
    public ConcretePiece(){
        this.eatenPieces = 0;
        }
        public Player getOwner(){
            return this.owner;
        }

        public void upDateEatenPieces(){
            eatenPieces++;
    }
    public int getEatenPieces(){
        return this.eatenPieces;
    }
}

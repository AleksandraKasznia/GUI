package sample;

public interface GroupBy {
    DataFrame max();
    DataFrame min();
    DataFrame mean() throws Exception;
    DataFrame std() throws Exception;
    DataFrame sum();
    DataFrame var() throws Exception;
    DataFrame median();
    DataFrame apply(Applyable a);
}

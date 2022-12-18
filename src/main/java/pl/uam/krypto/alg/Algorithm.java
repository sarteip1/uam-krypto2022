package pl.uam.krypto.cw;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Algorithm {


    public static void main(String[] args) {

        BigInteger e =
                new BigInteger("2219702669760051625529760071259189046161364151701596790770763259600544290997125107128138578832480323854037838605599695123440903054424577956799678397891626783444723950147784407335462559143107157658471735164714153971357443698994082727673072343180069044835094856719244582969485137575845153825021391095268519544748057926663150576101990156077844973202826679622719216615756960610764785110408304311098865781072786879379296360025429207038042833064515876868608188436266546466015175298619766069707237580766787423687287858279125035537409323009740621048068813783768774814593993312720811077575752373741693972477513");
        BigInteger d =
                new BigInteger("9738454175598488918517912045396815318351885031131011603301149540233201870415928124228184903947308481461717153640402767289853198952704967449300122329014740408508653613839688094250923162490670540988214688775753190900423588412005697560323304500348114898045236656807283167901253083798426709790746938525240264995502098847606530252043043212677911465343705421183831116604350283789270965024124861992541018116786274867535581082248878546385006259988838129620903989258127062367035340066868353921340378027331177496332241490297041686454303452932424111634076797215417394272455217584601075851777273706083879476230809");
        BigInteger n =
                new BigInteger("14205142842144491469901035779943007321473952670460614909740188710462796861921791780746014298824348546889748863603913825380912304112461129061114480661500416910991853573649055897001583708234998530660447745535711467407798340361335928981312718926721467943464464347521000503179497153112764130114342341251457556854374337702225661788558784747007799183865452550277915792606190524979919835785502848268656744723582283945123371679980696891117277548547543492116459573915049465031893477375432302554045103150951955486083526016584926750095118984741954481489582827589374811855794969993254570253121737541317841105374871");

        // 1 krok
        Pair<BigInteger, BigInteger> st = st(e, d);
        BigInteger s = st.getKey();
        BigInteger t = st.getValue();

        //2 krok
        BigInteger a = generateRandom(n);

        //3 krok
        if(a.gcd(n).compareTo(BigInteger.ONE) > 0) {
            System.out.println("This is the way: " + a);
            return;
        }

        // 4 krok
        BigInteger v = a.modPow(t, n);

        //5 krok
        if(v.compareTo(BigInteger.ONE) == 0) {
            System.out.println("This is the way: ∅");
            return;
        }

        //6 ktok
        BigInteger v0;
        do {
            v0 = v.mod(n);
            v = v.modPow(BigInteger.TWO, n);
        } while (v.compareTo(BigInteger.ONE) != 0);

        //7 krok
        if(v0.mod(n).compareTo(n.subtract(BigInteger.ONE)) == 0) {
            System.out.println("This is the way: ∅");
            return;
        }

        //8 krok
        System.out.println("This is the way: " + v0.add(BigInteger.ONE).gcd(n));
        System.out.println("End");
    }

    private static Pair<BigInteger, BigInteger> st(BigInteger e, BigInteger d) {
        BigInteger x = e.multiply(d).subtract(BigInteger.ONE);
        BigInteger t = x.divide(BigInteger.TWO);
        return fixS(BigInteger.ONE, t);
    }

    private static Pair<BigInteger, BigInteger> fixS(BigInteger s, BigInteger t) {
        if(t.mod(BigInteger.TWO).compareTo(BigInteger.ZERO) == 0) {
            return fixS(s.add(BigInteger.ONE), t.divide(BigInteger.TWO));
        }
        return Pair.of(s, t);
    }

    private static BigInteger generateRandom(BigInteger upperLimit) {
        SecureRandom random = new SecureRandom();
        BigInteger randomBigInt;
        do {
            randomBigInt = new BigInteger(upperLimit.bitLength(), random);
        } while (randomBigInt.compareTo(upperLimit) >= 0);
        return randomBigInt;
    }

}

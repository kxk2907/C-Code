package bank;

/**
 * 
 * @author Karthikeyan Karur Balu Lab - 3 submission LoanPolicy.aj
 * 
 *         The following code provides pointcuts and advises around the methods
 *         credit and newPeriod.
 * 
 *         Methods advised : CreditAccount.credit && CreditAccount.newPeriod
 * 
 *         pointcuts : executeCredit creation executeNewPeriod
 */
public aspect LoanPolicy pertarget(creation(CreditAccount)){

	/**
	 * pointcut creation is used to maintain multiple accounts at the same time
	 * with each advises instance and variable maintained for each CreditAccount
	 * 
	 * @param acca
	 *            - Capturing the target(acca) for this pointcut
	 */
	pointcut creation(CreditAccount acca) :
		execution(CreditAccount.new(..)) && target(acca);

	public static final Integer MAX_MISSED = 2;
	public static final Integer MIN_MADE = 6;
	Integer missed = 0;
	Integer made = 0;
	boolean onlyone = false;
	boolean reach = false;

	/**
	 * pointcut for CreditAccount.credit(Currency). It captures the target
	 * amount
	 * 
	 * @param amount
	 *            - amount is captured before any proceed.
	 */
	pointcut executeCredit(Currency amount): 
		execution(void CreditAccount.credit(Currency)) && 
		target(CreditAccount) &&
		args(amount);

	/**
	 * pointcut around newPeriod(). This is used for Interest update
	 */
	pointcut executeNewPeriod():
		execution(void CreditAccount.newPeriod()) &&
		target(CreditAccount);

	/**
	 * Advice for counting the credits made if missed reaches MAX_MISSED flag
	 * onlyone is set if more than one payment is made during is a cycle
	 * 
	 * @param amount
	 *            - amount that is proceeded forward
	 */
	void around(Currency amount): 
		executeCredit(amount) && target(CreditAccount)  {
		onlyone = true;
		missed = 0;
		if (reach)
			made++;
		else
			made = 0;
		proceed(amount);
	}

	/**
	 * Advice for updating the interest rate based on MAX_MISSED and MIN_MADE
	 * 
	 * Logic Used 1. If !onlyone payment made it is a missed payment => missed
	 * ++ Compare missed with MAX_MISSED -> Do the needful 2. Else payment made
	 * (proceed) if(reach) // reach is set if missed >= MAX_MISSED proceed with
	 * the rate until made < MIN_MADE if(made reaches MIN_MADE) reset everything
	 * 
	 * @param acca
	 *            - acca is captured to update the Interest rate
	 */
	void around(CreditAccount acca): 
		executeNewPeriod() && target(acca) {
		if (!onlyone) {
			missed++;
			if (reach) {
				missed = 0;
				made = 0;
			}
			if (missed < MAX_MISSED)
				proceed(acca);
			else if (missed >= MAX_MISSED) {
				reach = true;
				missed = 0;
				acca.setInterest((float) 0.025);
				proceed(acca);
			}
		} else {
			if (reach) {
				if (made < MIN_MADE) {
					proceed(acca);
				} else if (made >= MIN_MADE) {
					acca.setInterest((float) 0.005);
					made = 0;
					missed = 0;
					reach = false;
					proceed(acca);
				}
			} else {
				proceed(acca);
			}
		}
		onlyone = false;
	}
}


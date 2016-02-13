package drones;

import jsprit.core.algorithm.state.InternalStates;
import jsprit.core.algorithm.state.StateManager;
import jsprit.core.problem.Capacity;
import jsprit.core.problem.constraint.HardActivityConstraint;
import jsprit.core.problem.misc.JobInsertionContext;
import jsprit.core.problem.solution.route.activity.TourActivity;

/**
 * @author Orestis Melkonian
 */
public class DeliverAfterPickupConstraint implements HardActivityConstraint {

    private StateManager stateManager;

    DeliverAfterPickupConstraint(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override
    public ConstraintsStatus fulfilled(JobInsertionContext iFacts, TourActivity prevAct, TourActivity newAct, TourActivity nextAct, double prevActDepTime) {
        if (isPickup(prevAct) && isPickup(newAct)) {
            int prevCap = getSum(prevAct.getSize());
            int newCap = getSum(newAct.getSize());
            int sum = prevCap + newCap;

            if (sum > Data.maxPayload)
                return ConstraintsStatus.NOT_FULFILLED;
        }
        if (isPickup(newAct) && isPickup(nextAct)) {
            int newCap = getSum(newAct.getSize());
            int nextCap = getSum(nextAct.getSize());
            int sum = newCap + nextCap;

            if (sum > Data.maxPayload)
                return ConstraintsStatus.NOT_FULFILLED;
        }
        return ConstraintsStatus.FULFILLED;
    }

    private boolean isPickup(TourActivity act) {
        return act.getName().equals("pickupShipment");
    }

    private boolean isDelivery(TourActivity act) {
        return act.getName().equals("deliverShipment");
    }

    private Capacity getLoadAtPreviousAct(TourActivity prevAct) {
        Capacity prevLoad = stateManager.getActivityState(prevAct, InternalStates.LOAD, Capacity.class);
        if (prevLoad != null) return prevLoad;
        else return Capacity.Builder.newInstance().build();
    }

    private int getSum(Capacity capacity) {
        int sum = 0;
        for (int i = 0; i < capacity.getNuOfDimensions(); i++)
            sum += capacity.get(i);
        return sum;
    }
}

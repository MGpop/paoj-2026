package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.CannotCancelFinalOrderException;
import com.pao.laboratory07.exercise1.exceptions.CannotRevertInitialOrderStateException;
import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;

import java.util.Stack;

public class Order {
    private OrderState state;
    private Stack<OrderState> history = new Stack<>();

    public Order(OrderState state) {
        this.state = state;
    }

    public void nextState() throws OrderIsAlreadyFinalException {
        if (isFinalState()) {
            throw new OrderIsAlreadyFinalException();
        }

        history.push(state);

        switch (state) {
            case PLACED -> state = OrderState.PROCESSED;
            case PROCESSED -> state = OrderState.SHIPPED;
            case SHIPPED -> state = OrderState.DELIVERED;
        }

        System.out.println("Order state updated to: " + state);
    }

    public void cancel() throws CannotCancelFinalOrderException {
        if (isFinalState()) {
            throw new CannotCancelFinalOrderException();
        }

        history.push(state);
        state = OrderState.CANCELED;

        System.out.println("Order has been canceled.");
    }

    public void undoState() throws CannotRevertInitialOrderStateException {
        if (history.empty()) {
            throw new CannotRevertInitialOrderStateException();
        }

        state = history.pop();
        System.out.println("Order state reverted to: " + state);
    }

    private boolean isFinalState() {
        return state == OrderState.DELIVERED || state == OrderState.CANCELED;
    }
}
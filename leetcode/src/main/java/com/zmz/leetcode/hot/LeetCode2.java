package com.zmz.leetcode.hot;

/**
 * leetcode 2 两数相加
 * <p>
 * <p>
 * 给你两个 非空 的链表，表示两个非负的整数。它们每位数字都是按照 逆序 的方式存储的，并且每个节点只能存储 一位 数字。
 * <p>
 * 请你将两个数相加，并以相同形式返回一个表示和的链表。
 * <p>
 * 你可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 */

public class LeetCode2 {
    public static void main(String[] args) {
        ListNode l1 = new ListNode(2);
        ListNode l2 = new ListNode(4);
        ListNode l3 = new ListNode(3);
        l1.next=l2;
        l2.next=l3;
        ListNode r1 = new ListNode(5);
        ListNode r2 = new ListNode(6);
        ListNode r3 = new ListNode(4);
        r1.next=r2;
        r2.next=r3;
        addTwoNumbers(l1,r1);
    }


    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        String value1 = "";
        String value2 = "";
        while (l1 != null) {
            value1 = String.valueOf(l1.val) + value1;
            l1 = l1.next;
        }
        int a = Integer.parseInt(value1);
        while (l2 != null) {
            value2 = String.valueOf(l2.val) + value2;
            l2 = l2.next;
        }
        int b = Integer.parseInt(value2);
        int res = a+b;
        System.err.println(res);
        return null;
    }

}


class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}
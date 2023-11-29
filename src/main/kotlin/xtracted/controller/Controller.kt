package xtracted.controller

interface  Controller<A: Action> {
    fun trigger(action: A)
}
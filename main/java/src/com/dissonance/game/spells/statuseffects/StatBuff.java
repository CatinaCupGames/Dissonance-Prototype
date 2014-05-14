package com.dissonance.game.spells.statuseffects;

import com.dissonance.framework.game.combat.spells.StatusEffect;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

public class StatBuff extends StatusEffect {
    private int oa, od, os, ov, ost, ow, of, om, omr;
    private int a, d, s, v, st, w, f, m, mr;
    public StatBuff(long duration, float value, int attack_boost, int defense_boost, int speed_boost, int vigor_boost, int stamina_boost, int willpower_boost, int focus_boost, int marksmenship_boost, int magicresistance_boost) {
        super(duration, value);

        this.a = attack_boost;
        this.d = defense_boost;
        this.s = speed_boost;
        this.v = vigor_boost;
        this.st = stamina_boost;
        this.w = willpower_boost;
        this.f = focus_boost;
        this.m = marksmenship_boost;
        this.mr = magicresistance_boost;
    }

    @Override
    protected void onStart(CombatSprite owner) {
        oa = owner.getAttack();
        od = owner.getDefense();
        os = owner.getSpeed();
        ov = owner.getVigor();
        ost = owner.getStamina();
        ow = owner.getWillPower();
        of = owner.getFocus();
        om = owner.getMarksmanship();
<<<<<<< HEAD

=======
>>>>>>> fd147984b6aa41c540a32bb51cfdadb7be9dca2b

        owner.setAttack(owner.getAttack() + a);
        owner.setDefense(owner.getDefense() + d);
        owner.setSpeed(owner.getSpeed() + s);
        owner.setVigor(owner.getVigor() + v);
        owner.setStamina(owner.getStamina() + st);
        owner.setWillpower(owner.getWillPower() + w);
        owner.setFocus(owner.getFocus() + f);
        owner.setMarksmanship(owner.getMarksmanship() + m);
<<<<<<< HEAD


=======
>>>>>>> fd147984b6aa41c540a32bb51cfdadb7be9dca2b
    }

    @Override
    protected void onInflict(CombatSprite owner) { }

    @Override
    protected void onEnd(CombatSprite owner) {
        owner.setAttack(oa);
        owner.setDefense(od);
        owner.setSpeed(os);
        owner.setVigor(ov);
        owner.setStamina(ost);
        owner.setWillpower(ow);
        owner.setFocus(of);
        owner.setMarksmanship(om);
<<<<<<< HEAD

=======
>>>>>>> fd147984b6aa41c540a32bb51cfdadb7be9dca2b
    }
}

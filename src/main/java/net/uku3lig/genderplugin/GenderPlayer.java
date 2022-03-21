package net.uku3lig.genderplugin;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "uuid")
public class GenderPlayer {
    private UUID uuid;
    private int gender;
    private float bustSize;

    private boolean hurtSounds;
    private boolean breastPhysics;
    private boolean breastPhysicsInArmor;
    private boolean showInArmor;

    private float breastOffsetX;
    private float breastOffsetY;
    private float breastOffsetZ;
    private float breastCleavage;

    private boolean dualPhysics;
    private float bounceMultiplier;
    private float floppyMultiplier;
}

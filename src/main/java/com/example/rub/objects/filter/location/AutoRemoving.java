package com.example.rub.objects.filter.location;

import com.example.rub.functionalities.locations.Locality;

public interface AutoRemoving {
    void removeChoice(Choice choice);
    void setAssigned(Locality locality);
}

package apps.alquran.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import apps.alquran.R;
import apps.alquran.adapter.AyahAdapter;
import apps.alquran.api.ApiClient;
import apps.alquran.api.ApiService;
import apps.alquran.data.Ayat;
import apps.alquran.utils.Preferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuAlQuranFragment extends Fragment {

    private RecyclerView recyclerViewAyah;
    private AyahAdapter ayahAdapter;
    private List<Ayat> ayahList;
    private String surahNumber;
    private String surahName;

    public MenuAlQuranFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_al_quran, container, false);

        if (getArguments() != null) {
            surahNumber = getArguments().getString("nomor");
            surahName = getArguments().getString("name");
            Preferences.saveLastOpenedSurah(getContext(), surahNumber, surahName);
        } else {
            String[] lastOpenedSurah = Preferences.getLastOpenedSurah(getContext());
            surahNumber = lastOpenedSurah[0];
            surahName = lastOpenedSurah[1];
        }

        if (surahNumber == null || surahName == null) {
            // Handle case where no surah has been selected
            return view;
        }

        TextView txtSurahName = view.findViewById(R.id.surahName);
        txtSurahName.setText(surahName); // Set the surah name

        recyclerViewAyah = view.findViewById(R.id.recyclerViewAyah);
        recyclerViewAyah.setLayoutManager(new LinearLayoutManager(getContext()));

        ApiService apiService = ApiClient.getApiClient().create(ApiService.class);
        String apiUrl = "https://api.npoint.io/99c279bb173a6e28359c/surat/" + surahNumber;
        Call<List<Ayat>> call = apiService.getSurahDetails(apiUrl);
        call.enqueue(new Callback<List<Ayat>>() {
            @Override
            public void onResponse(Call<List<Ayat>> call, Response<List<Ayat>> response) {
                ayahList = response.body();
                ayahAdapter = new AyahAdapter(ayahList);
                recyclerViewAyah.setAdapter(ayahAdapter);
            }

            @Override
            public void onFailure(Call<List<Ayat>> call, Throwable t) {
                // Handle error
            }
        });

        return view;
    }
}

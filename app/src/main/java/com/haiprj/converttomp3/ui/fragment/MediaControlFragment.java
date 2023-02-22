package com.haiprj.converttomp3.ui.fragment;

import android.content.res.ColorStateList;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.haiprj.converttomp3.AppCallback;
import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.databinding.FragmentMusicEditBarBinding;
import com.haiprj.converttomp3.models.FileModel;
import com.haiprj.converttomp3.utils.AppUtils;
import com.haiprj.converttomp3.widget.TimeLineView;

import java.io.IOException;

public class MediaControlFragment extends BaseFragment<FragmentMusicEditBarBinding> {

    private MediaPlayer mediaPlayer;
    private FileModel fileModel;

    private AppCallback callback;
    public MediaControlFragment(FileModel FileModel) {
        this.fileModel = FileModel;
    }

    @Override
    protected void initView() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(mp -> {
            binding.mediaController.timeSeekBar.setMax(1000);
            binding.timeLine.init(new TimeLineView.TimeLineListener() {
                @Override
                public int getCurrentPosition() {
                    binding.mediaController.currentTime.setText(AppUtils.stringForTime(mediaPlayer.getCurrentPosition()));
                    binding.mediaController.timeSeekBar.setProgress((int) (1000L * mp.getCurrentPosition() / mp.getDuration()), true);
                    return mediaPlayer.getCurrentPosition();
                }

                @Override
                public int getDuration() {
                    return mediaPlayer.getDuration();
                }
            });
            binding.mediaController.duration.setText(AppUtils.stringForTime(mediaPlayer.getDuration()));
            binding.timeLine.setPrepared();
            mediaPlayer.start();
            binding.mediaController.play.setImageResource(R.drawable.ic_pause);
        });
        mediaPlayer.setOnCompletionListener(mp -> {
            binding.timeLine.setComplete();
            binding.mediaController.play.setImageResource(R.drawable.ic_play);
        });
        reload();


    }

    public void setCallback(AppCallback callback) {
        this.callback = callback;
    }

    private void reload() {
        Glide.with(this).load(fileModel.getFileUri()).into(binding.musicImage);
        binding.fileName.setText(fileModel.getDisplayName());
        try {
            mediaPlayer.setDataSource(requireContext(), fileModel.getFileUri());
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void addEvent() {
        binding.mediaController.randomMusic.setOnClickListener(v -> {
            ((ImageView) v).setImageTintList(ColorStateList.valueOf(requireContext().getColor(R.color.app_color)));
        });
        binding.mediaController.play.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                binding.mediaController.play.setImageResource(R.drawable.ic_play);
                mediaPlayer.pause();
            }
            else {
                binding.mediaController.play.setImageResource(R.drawable.ic_pause);
                mediaPlayer.start();
            }
        });
        binding.back.setOnClickListener(v -> {
            callback.action("finish");
        });

        binding.share.setOnClickListener(v -> {
            AppUtils.shareFile(requireContext(), fileModel.getFileUri());
        });

        binding.mediaController.next.setOnClickListener(v -> {
            callback.action("next", fileModel);
        });

        binding.mediaController.previous.setOnClickListener(v -> {
            callback.action("previous", fileModel);
        });
        binding.mediaController.timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo((int) (seekBar.getProgress() * mediaPlayer.getDuration() / 1000L));
            }
        });
    }

    public void loadData(FileModel FileModel) {
        this.fileModel = FileModel;
        mediaPlayer.reset();
        reload();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.timeLine.destroy();
        mediaPlayer.reset();
        mediaPlayer.release();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_music_edit_bar;
    }

}
